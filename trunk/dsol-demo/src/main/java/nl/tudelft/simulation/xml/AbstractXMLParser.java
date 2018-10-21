package nl.tudelft.simulation.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.xerces.parsers.DOMParser;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.XMLReaderFactory;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.language.io.URLResource;

/**
 * <br>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a>.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public abstract class AbstractXMLParser
{
    /** schema validation on/off. */
    private boolean validateSchema;

    /** the URL of the file to parse. */
    private URL url;

    /** the URL of the schema file. */
    private URL schema;

    /** the namespace of the schema. */
    private String schemaNamespace;

    /**
     * Parses an XML file and validates it against a schema XSD. Explicitly call parse() from the extended class, after
     * other initialization tasks have been carried out. The parse() method will, after schema validation and xml-file
     * validation, call the parse(Element) method that has been declared in the extended class.
     * @param url URL; the URL of the XML file to read.
     * @param schema URL; the URL of the XSD schema file to validate against.
     * @param schemaNamespace String; the namespace of the schema
     * @param validateSchema boolean; whether to validate the schema file.
     */
    public AbstractXMLParser(final URL url, final URL schema, final String schemaNamespace,
            final boolean validateSchema)
    {
        this.validateSchema = validateSchema;
        this.url = url;
        this.schema = schema;
        this.schemaNamespace = schemaNamespace;
    }

    /**
     * This method carries out the task of parsing an XML file and validates it against a schema XSD. dsol-xml now
     * contains the 2001 definitions of the schema files: XMLSchema.xsd, XMLSchema.dtd, xml.xsd, and datatypes.dtd to
     * validate against. When no network connection is available, these definitions can help for validation when
     * <code>validation</code> is true.
     * @return the JDOM root element of the XML file
     * @throws Exception on failure
     */
    protected Element parse() throws Exception
    {
        if (this.validateSchema)
        {
            // create the parser to validate the schema file with SAX
            XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            parser.setErrorHandler(new ValidHandler());
            // turns on Schema Validation with Xerces
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            // fatal error handler
            parser.setEntityResolver(new RelativePathResolver());
            parser.setErrorHandler(new ValidHandler());
            parser.parse(this.schema.toExternalForm());
        }
        // build the DOM document for the content -- validation on
        DOMParser domParser = new DOMParser();
        domParser.setFeature("http://xml.org/sax/features/validation", true);
        domParser.setFeature("http://apache.org/xml/features/validation/schema", true);
        domParser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
                this.schemaNamespace + " " + this.schema.toExternalForm());
        domParser.setEntityResolver(new RelativePathResolver());
        domParser.setErrorHandler(new ValidHandler());
        // System.exit(0);
        domParser.parse(this.url.toExternalForm());
        Document document = domParser.getDocument();
        DOMBuilder builder = new DOMBuilder();
        org.jdom2.Document jdomDocument = builder.build(document);
        Element xmlRootElement = jdomDocument.getRootElement();
        return xmlRootElement;
    }

    /**
     * The actual parsing method to implement, based on the availability of the entire JDOM tree, starting from the root
     * element.
     * @param xmlRootElement Element; the root element
     * @throws Exception on failure
     */
    protected abstract void parse(final Element xmlRootElement) throws Exception;

    /**
     * The ValidHandler ErrorHandler gives a more extensive explanation about errors that occur during parsing. If
     * needed, this inner class can be overridden to provide other forms of error handling. <br>
     * <p>
     * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
     * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which
     * can be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
     * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
     * </p>
     * @author <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
     */
    private static class ValidHandler implements ErrorHandler
    {
        /**
         * format the exception with line number, column number, etc.
         * @param exception SAXParseException; the excpetion to format
         * @return the String
         */
        private String formatError(final SAXParseException exception)
        {
            return "SAXParseException: \nsystemId=" + exception.getSystemId() + "\npublicId=" + exception.getSystemId()
                    + "\nMessage=" + exception.getMessage() + "\nline,col=" + exception.getLineNumber() + ","
                    + exception.getColumnNumber();
        }

        /** {@inheritDoc} */
        @Override
        public void warning(final SAXParseException exception)
        {
            // ignore, but log
            SimLogger.always().warn(exception, formatError(exception));
        }

        /** {@inheritDoc} */
        @Override
        public void error(final SAXParseException e) throws SAXException
        {
            throw new SAXException(formatError(e));
        }

        /** {@inheritDoc} */
        @Override
        public void fatalError(final SAXParseException e) throws SAXException
        {
            throw new SAXException(formatError(e));
        }
    }

    /**
     * The relative path resolver takes care of resolving xsd-files or xml-files, that are in the current classpath.
     * Example use is the <code>include</code> tag in the schema file, that can now point to a relative path. <br>
     * <p>
     * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
     * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which
     * can be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
     * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
     * </p>
     * @author <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
     */
    private static class RelativePathResolver implements EntityResolver2
    {
        /** the fallback defaultHandler2 */
        private DefaultHandler2 defaultHandler2 = new DefaultHandler2();

        /** {@inheritDoc} */
        @Override
        public InputSource getExternalSubset(final String name, final String baseURI) throws SAXException, IOException
        {
            return this.defaultHandler2.getExternalSubset(name, baseURI);
        }

        /** {@inheritDoc} */
        @Override
        public InputSource resolveEntity(final String name, final String publicId, final String baseURI,
                final String systemId) throws SAXException, IOException
        {
            return this.defaultHandler2.resolveEntity(name, publicId, baseURI, systemId);
        }

        /** {@inheritDoc} */
        @Override
        public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException
        {
            URL url;
            if (systemId.startsWith("file:"))
            {
                url = URLResource.getResource(systemId.substring(5));
            }
            else
            {
                url = URLResource.getResource(systemId);
            }
            InputStream localStream = URLResource.getResourceAsStream(url.toExternalForm());
            if (localStream != null)
            {
                return new InputSource(localStream);
            }
            return this.defaultHandler2.resolveEntity(publicId, systemId);
        }
    }
}
