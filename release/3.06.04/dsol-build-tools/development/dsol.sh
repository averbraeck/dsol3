#!/bin/bash
echo deploy dsol

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ] || [ -z "$4" ]; then
  echo usage: $0 OLD_SNAPSHOT_VERSION DEPLOY_VERSION NEW_SNAPSHOT_VERSION \"svn deploy comment\"
  echo e.g.,: $0 3.02.00-SNAPSHOT 3.02.00 3.02.01-SNAPSHOT \"deployed version 3.02.00\"
  exit
fi

OLDSNAPSHOTVERSION=$1
DEPLOYVERSION=$2
NEWSNAPSHOTVERSION=$3
SVNCOMMENT=$4

cd /cygdrive/e/java/dsol300/workspace

MODULES="dsol-build-tools dsol dsol-base dsol-event dsol-naming dsol-interpreter dsol-core dsol-introspection dsol-swing dsol-web dsol-demo"
# dsol-hla dsol-zmq dsol-akka


# check preconditions

for i in $MODULES; do

  if [ ! -d $i ]; then
    echo folder $i not found...
    exit
  fi

  cd $i
  if [ ! -f pom.xml ]; then
    echo pom.xml not found in folder $i
    exit
  fi


  if [ -z `grep $OLDSNAPSHOTVERSION pom.xml` ]; then
    echo version $OLDSNAPSHOTVERSION not found in pom.xml in $i
    exit
  fi

  cd ..

done


# modify the POM files

for i in $MODULES; do

  cd $i

  sed -i "s/$OLDSNAPSHOTVERSION/$DEPLOYVERSION/" pom.xml
  chmod 777 pom.xml 

  cd ..

done


# run mvn on ech project and see whether it works fine

for i in $MODULES; do

  cd $i

  mvn clean install
  rc=$?
  if [ $rc -ne 0 ] ; then
    echo mvn clean install for project $i failed with exit code $rc
    #change back the POM files
    cd ..
    for j in $MODULES; do
      cd $j
      sed -i "s/$DEPLOYVERSION/$OLDSNAPSHOTVERSION/" pom.xml
      chmod 777 pom.xml
      cd ..
    done 
    exit $rc
  fi 

  cd ..

done


# deploy the sites and see whether it runs fine

for i in $MODULES; do

  cd $i

  if [ $i == "dsol-build-tools" ] ; then
    mvn deploy
  else
    mvn deploy site-deploy 
  fi
  rc=$?
  if [ $rc -ne 0 ] ; then
    echo mvn deploy site-deploy for project $i failed with exit code $rc
    #change back the POM files
    cd ..
    for j in $MODULES; do
      cd $j
      sed -i "s/$DEPLOYVERSION/$OLDSNAPSHOTVERSION/" pom.xml
      chmod 777 pom.xml
      cd ..
    done 
    exit $rc
  fi 

  cd ..

done


# delete the svn version tree if it exists in the release folder

svn delete https://svn.tbm.tudelft.nl/DSOL3/release/$DEPLOYVERSION -m "delete incomplete release $DEPLOYVERSION"
svn mkdir https://svn.tbm.tudelft.nl/DSOL3/release/$DEPLOYVERSION -m "$SVNCOMMENT"


# copy dsol-akka, dsol-hla and dsol-zmq projects (not recompiled or committed yet)
svn copy -rHEAD https://svn.tbm.tudelft.nl/DSOL3/trunk/dsol-akka https://svn.tbm.tudelft.nl/DSOL3/release/$DEPLOYVERSION -m "$SVNCOMMENT"
svn copy -rHEAD https://svn.tbm.tudelft.nl/DSOL3/trunk/dsol-hla https://svn.tbm.tudelft.nl/DSOL3/release/$DEPLOYVERSION -m "$SVNCOMMENT"
svn copy -rHEAD https://svn.tbm.tudelft.nl/DSOL3/trunk/dsol-zmq https://svn.tbm.tudelft.nl/DSOL3/release/$DEPLOYVERSION -m "$SVNCOMMENT"


# write everything to svn

for i in $MODULES; do

  cd $i

  svn commit -m "$SVNCOMMENT" pom.xml
  # svn mkdir https://svn.tbm.tudelft.nl/DSOL3/release/$DEPLOYVERSION/$i -m "$SVNCOMMENT"
  svn copy -rHEAD https://svn.tbm.tudelft.nl/DSOL3/trunk/$i https://svn.tbm.tudelft.nl/DSOL3/release/$DEPLOYVERSION -m "$SVNCOMMENT"

  cd ..

done


# change to new snapshot version and commit to SVN

for i in $MODULES; do

  cd $i

  sed -i "s/$DEPLOYVERSION/$NEWSNAPSHOTVERSION/" pom.xml
  chmod 777 pom.xml 

  svn commit -m "$SVNCOMMENT" pom.xml

  cd ..

done


echo Finished committing version $DEPLOYVERSION of DSOL3

