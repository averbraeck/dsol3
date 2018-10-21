package nl.tudelft.simulation.dsol.web.animation;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;

/**
 * HTMLGraphics.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class HTMLGraphics2D extends Graphics2D
{
    Color background = Color.WHITE;

    Color color = Color.BLACK;

    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

    Canvas canvas = new Canvas();

    FontMetrics fontMetrics = canvas.getFontMetrics(font);

    Paint paint = Color.BLACK;

    Stroke stroke = new BasicStroke();

    RenderingHints renderingHints = new RenderingHints(new HashMap<Key, Object>());

    AffineTransform affineTransform = new AffineTransform();

    Composite composite = AlphaComposite.Clear;

    StringBuffer commands = new StringBuffer();

    public void clearCommand()
    {
        this.commands = new StringBuffer();
        this.commands.append("<animate>\n");
    }

    public String getCommands()
    {
        this.commands.append("</animate>\n");
        return this.commands.toString();
    }

    protected void addDraw(String drawCommand, Object... params)
    {
        this.commands.append("<draw>" + drawCommand);
        for (Object param : params)
        {
            this.commands.append("," + param.toString());
        }
        this.commands.append("</draw>\n");
    }

    /**
     * add AffineTransform to the command.
     */
    protected void addAffineTransform()
    {
        this.commands.append(",");
        this.commands.append(this.affineTransform.getScaleX());
        this.commands.append(",");
        this.commands.append(this.affineTransform.getShearX());
        this.commands.append(",");
        this.commands.append(this.affineTransform.getShearY());
        this.commands.append(",");
        this.commands.append(this.affineTransform.getScaleY());
        this.commands.append(",");
        this.commands.append(this.affineTransform.getTranslateX());
        this.commands.append(",");
        this.commands.append(this.affineTransform.getTranslateY());
    }

    /**
     * add Color to the command.
     */
    protected void addColor()
    {
        this.commands.append(",");
        this.commands.append(this.color.getRed());
        this.commands.append(",");
        this.commands.append(this.color.getGreen());
        this.commands.append(",");
        this.commands.append(this.color.getBlue());
        this.commands.append(",");
        this.commands.append(this.color.getAlpha());
        this.commands.append(",");
        this.commands.append(this.color.getTransparency());
    }

    /**
     * add font data to the command.
     */
    protected void addFontData()
    {
        this.commands.append(",");
        String javaFontName = this.font.getFontName();
        String htmlFontName = "sans-serif";
        if (javaFontName.contains("Times"))
        {
            htmlFontName = "serif";
        }
        this.commands.append(htmlFontName);
        this.commands.append(",");
        this.commands.append(this.font.getSize2D());
        this.commands.append(",");
        this.commands.append("normal"); // TODO
    }

    /**
     * Send command, transform.m11(h-scale), transform.m12(h-skew), transform.m21(v-skew), transform.m22(v-scale),
     * transform.dx(h-translate), transform.dy(v-translate), color.r, golor.g, color.b, color.alpha, color.transparency,
     * params...
     * @param drawCommand
     * @param params
     */
    protected void addTransformDraw(String drawCommand, Object... params)
    {
        this.commands.append("<transformDraw>" + drawCommand);
        addAffineTransform();
        addColor();
        for (Object param : params)
        {
            this.commands.append("," + param.toString());
        }
        this.commands.append("</transformDraw>\n");
    }

    /**
     * Send string, 0=command, 1=transform.m11(h-scale), 2=transform.m12(h-skew), 3=transform.m21(v-skew),
     * 4=transform.m22(v-scale), 5=transform.dx(h-translate), 6=transform.dy(v-translate), 7=color.r, 8=color.g,
     * 9=color.b, 10=color.alpha, 11=color.transparency, 12=fontname, 13=fontsize, 14=fontstyle(normal/italic/bold),
     * 15=x, 16=y, 17=text.
     * @param drawCommand
     * @param params
     */
    protected void addTransformText(String drawCommand, Object... params)
    {
        this.commands.append("<transformText>" + drawCommand);
        addAffineTransform();
        addColor();
        addFontData();
        for (Object param : params)
        {
            this.commands.append("," + param.toString());
        }
        this.commands.append("</transformText>\n");
    }

    /** {@inheritDoc} */
    @Override
    public void draw(Shape shape)
    {
        System.out.println("HTMLGraphics2D.draw(shape)");
        if (shape instanceof Ellipse2D.Double)
        {
            Ellipse2D.Double ellipse = (Ellipse2D.Double) shape;
            addTransformDraw("drawOval", ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
        else if (shape instanceof Ellipse2D.Float)
        {
            Ellipse2D.Float ellipse = (Ellipse2D.Float) shape;
            addTransformDraw("drawOval", ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
        else if (shape instanceof Line2D.Double)
        {
            Line2D.Double line = (Line2D.Double) shape;
            addTransformDraw("drawLine", line.x1, line.y1, line.x2, line.y2);
        }
        else if (shape instanceof Line2D.Float)
        {
            Line2D.Float line = (Line2D.Float) shape;
            addTransformDraw("drawLine", line.x1, line.y1, line.x2, line.y2);
        }
        else if (shape instanceof Rectangle2D.Double)
        {
            Rectangle2D.Double rect = (Rectangle2D.Double) shape;
            addTransformDraw("drawRect", rect.x, rect.y, rect.width, rect.height);
        }
        else if (shape instanceof Rectangle2D.Float)
        {
            Rectangle2D.Float rect = (Rectangle2D.Float) shape;
            addTransformDraw("drawRect", rect.x, rect.y, rect.width, rect.height);
        }

    }

    /** {@inheritDoc} */
    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs)
    {
        System.out.println("HTMLGraphics2D.drawImage()");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y)
    {
        System.out.println("HTMLGraphics2D.drawImage()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform)
    {
        System.out.println("HTMLGraphics2D.drawRenderedImage()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform)
    {
        System.out.println("HTMLGraphics2D.drawRenderableImage()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawString(String str, int x, int y)
    {
        System.out.println("HTMLGraphics2D.drawString()");
        addTransformText("drawString", x, y, str);
    }

    /** {@inheritDoc} */
    @Override
    public void drawString(String str, float x, float y)
    {
        System.out.println("HTMLGraphics2D.drawString()");
        addTransformText("drawString", x, y, str);
    }

    /** {@inheritDoc} */
    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y)
    {
        System.out.println("HTMLGraphics2D.drawString()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y)
    {
        System.out.println("HTMLGraphics2D.drawString()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y)
    {
        System.out.println("HTMLGraphics2D.drawGlyphVector()");
    }

    /** {@inheritDoc} */
    @Override
    public void fill(Shape s)
    {
        System.out.println("HTMLGraphics2D.fill()");
    }

    /** {@inheritDoc} */
    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke)
    {
        System.out.println("HTMLGraphics2D.hit()");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public GraphicsConfiguration getDeviceConfiguration()
    {
        System.out.println("HTMLGraphics2D.getDeviceConfiguration()");
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void setComposite(Composite comp)
    {
        System.out.println("HTMLGraphics2D.setComposite()");
    }

    /** {@inheritDoc} */
    @Override
    public void setPaint(Paint paint)
    {
        this.paint = paint;
        System.out.println("HTMLGraphics2D.setPaint()");
    }

    /** {@inheritDoc} */
    @Override
    public void setStroke(Stroke s)
    {
        this.stroke = s;
        System.out.println("HTMLGraphics2D.setStroke()");
    }

    /** {@inheritDoc} */
    @Override
    public void setRenderingHint(Key hintKey, Object hintValue)
    {
        this.renderingHints.put(hintKey, hintValue);
        System.out.println("HTMLGraphics2D.setRenderingHint()");
    }

    /** {@inheritDoc} */
    @Override
    public Object getRenderingHint(Key hintKey)
    {
        System.out.println("HTMLGraphics2D.getRenderingHint()");
        return this.renderingHints.get(hintKey);
    }

    /** {@inheritDoc} */
    @Override
    public void setRenderingHints(Map<?, ?> hints)
    {
        this.renderingHints.clear();
        this.renderingHints.putAll(hints);
        System.out.println("HTMLGraphics2D.setRenderingHints()");
    }

    /** {@inheritDoc} */
    @Override
    public void addRenderingHints(Map<?, ?> hints)
    {
        this.renderingHints.putAll(hints);
        System.out.println("HTMLGraphics2D.addRenderingHints()");
    }

    /** {@inheritDoc} */
    @Override
    public RenderingHints getRenderingHints()
    {
        System.out.println("HTMLGraphics2D.getRenderingHints()");
        return this.renderingHints;
    }

    /** {@inheritDoc} */
    @Override
    public void translate(int x, int y)
    {
        this.affineTransform.translate(x, y);
        System.out.println("HTMLGraphics2D.translate()");
    }

    /** {@inheritDoc} */
    @Override
    public void translate(double tx, double ty)
    {
        this.affineTransform.translate(tx, ty);
        System.out.println("HTMLGraphics2D.translate()");
    }

    /** {@inheritDoc} */
    @Override
    public void rotate(double theta)
    {
        this.affineTransform.rotate(theta);
        System.out.println("HTMLGraphics2D.rotate()");
    }

    /** {@inheritDoc} */
    @Override
    public void rotate(double theta, double x, double y)
    {
        this.affineTransform.rotate(theta, x, y);
        System.out.println("HTMLGraphics2D.rotate()");
    }

    /** {@inheritDoc} */
    @Override
    public void scale(double sx, double sy)
    {
        this.affineTransform.scale(sx, sy);
        System.out.println("HTMLGraphics2D.scale()");
    }

    /** {@inheritDoc} */
    @Override
    public void shear(double shx, double shy)
    {
        this.affineTransform.shear(shx, shy);
        System.out.println("HTMLGraphics2D.shear()");
    }

    /** {@inheritDoc} */
    @Override
    public void transform(AffineTransform Tx)
    {
        System.out.println("HTMLGraphics2D.transform()");
    }

    /** {@inheritDoc} */
    @Override
    public void setTransform(AffineTransform Tx)
    {
        this.affineTransform = (AffineTransform) Tx.clone();
        System.out.println("HTMLGraphics2D.setTransform()");
    }

    /** {@inheritDoc} */
    @Override
    public AffineTransform getTransform()
    {
        System.out.println("HTMLGraphics2D.getTransform()");
        return this.affineTransform;
    }

    /** {@inheritDoc} */
    @Override
    public Paint getPaint()
    {
        System.out.println("HTMLGraphics2D.getPaint()");
        return this.paint;
    }

    /** {@inheritDoc} */
    @Override
    public Composite getComposite()
    {
        System.out.println("HTMLGraphics2D.getComposite()");
        return this.composite;
    }

    /** {@inheritDoc} */
    @Override
    public void setBackground(Color color)
    {
        this.background = color;
        System.out.println("HTMLGraphics2D.setBackground()");
    }

    /** {@inheritDoc} */
    @Override
    public Color getBackground()
    {
        System.out.println("HTMLGraphics2D.getBackground()");
        return this.background;
    }

    /** {@inheritDoc} */
    @Override
    public Stroke getStroke()
    {
        System.out.println("HTMLGraphics2D.getStroke()");
        return this.stroke;
    }

    /** {@inheritDoc} */
    @Override
    public void clip(Shape s)
    {
        System.out.println("HTMLGraphics2D.clip()");
    }

    /** {@inheritDoc} */
    @Override
    public FontRenderContext getFontRenderContext()
    {
        System.out.println("HTMLGraphics2D.getFontRenderContext()");
        return new FontRenderContext(this.affineTransform, true, true);
    }

    /** {@inheritDoc} */
    @Override
    public Graphics create()
    {
        System.out.println("HTMLGraphics2D.create()");
        return new HTMLGraphics2D(); // TODO: clone
    }

    /** {@inheritDoc} */
    @Override
    public Color getColor()
    {
        System.out.println("HTMLGraphics2D.getColor()");
        return this.color;
    }

    /** {@inheritDoc} */
    @Override
    public void setColor(Color c)
    {
        this.color = c;
        System.out.println("HTMLGraphics2D.setColor()");
    }

    /** {@inheritDoc} */
    @Override
    public void setPaintMode()
    {
        System.out.println("HTMLGraphics2D.setPaintMode()");
    }

    /** {@inheritDoc} */
    @Override
    public void setXORMode(Color c1)
    {
        System.out.println("HTMLGraphics2D.setXORMode()");
    }

    /** {@inheritDoc} */
    @Override
    public Font getFont()
    {
        System.out.println("HTMLGraphics2D.getFont()");
        return this.font;
    }

    /** {@inheritDoc} */
    @Override
    public void setFont(Font font)
    {
        this.font = font;
        this.fontMetrics = this.canvas.getFontMetrics(this.font);
        System.out.println("HTMLGraphics2D.setFont()");
    }

    /** {@inheritDoc} */
    @Override
    public FontMetrics getFontMetrics(Font f)
    {
        System.out.println("HTMLGraphics2D.getFontMetrics()");
        return this.fontMetrics;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getClipBounds()
    {
        System.out.println("HTMLGraphics2D.getClipBounds()");
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void clipRect(int x, int y, int width, int height)
    {
        System.out.println("HTMLGraphics2D.clipRect()");
    }

    /** {@inheritDoc} */
    @Override
    public void setClip(int x, int y, int width, int height)
    {
        System.out.println("HTMLGraphics2D.setClip()");
    }

    /** {@inheritDoc} */
    @Override
    public Shape getClip()
    {
        System.out.println("HTMLGraphics2D.getClip()");
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void setClip(Shape clip)
    {
        System.out.println("HTMLGraphics2D.setClip()");
    }

    /** {@inheritDoc} */
    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy)
    {
        System.out.println("HTMLGraphics2D.copyArea()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        System.out.println("HTMLGraphics2D.drawLine()");
        addTransformDraw("drawLine", x1, y1, x2, y2);
    }

    /** {@inheritDoc} */
    @Override
    public void fillRect(int x, int y, int width, int height)
    {
        System.out.println("HTMLGraphics2D.fillRect()");
        addTransformDraw("fillRect", x, y, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public void clearRect(int x, int y, int width, int height)
    {
        System.out.println("HTMLGraphics2D.clearRect()");
        addTransformDraw("clearRect", x, y, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        System.out.println("HTMLGraphics2D.drawRoundRect()");
    }

    /** {@inheritDoc} */
    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        System.out.println("HTMLGraphics2D.fillRoundRect()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawOval(int x, int y, int width, int height)
    {
        System.out.println("HTMLGraphics2D.drawOval()");
        addTransformDraw("drawOval", x, y, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public void fillOval(int x, int y, int width, int height)
    {
        System.out.println("HTMLGraphics2D.fillOval()");
        addTransformDraw("fillOval", x, y, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        System.out.println("HTMLGraphics2D.drawArc()");
    }

    /** {@inheritDoc} */
    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        System.out.println("HTMLGraphics2D.fillArc()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
    {
        System.out.println("HTMLGraphics2D.fillPolyline()");
    }

    /** {@inheritDoc} */
    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
    {
        System.out.println("HTMLGraphics2D.drawPolygon()");
    }

    /** {@inheritDoc} */
    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
    {
        System.out.println("HTMLGraphics2D.fillPolygon()");
    }

    /** {@inheritDoc} */
    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer)
    {
        System.out.println("HTMLGraphics2D.drawImage()");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)
    {
        System.out.println("HTMLGraphics2D.drawImage()");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer)
    {
        System.out.println("HTMLGraphics2D.drawImage()");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer)
    {
        System.out.println("HTMLGraphics2D.drawImage()");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
            ImageObserver observer)
    {
        System.out.println("HTMLGraphics2D.drawImage()");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
            Color bgcolor, ImageObserver observer)
    {
        System.out.println("HTMLGraphics2D.drawImage()");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void dispose()
    {
        System.out.println("HTMLGraphics2D.dispose()");
    }

}
