/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi_editor;

//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import javax.swing.JPanel;

import java.awt.*;
import java.util.ArrayList;
//import java.awt.geom.Line2D;
import javax.swing.*;

import javax.swing.*;

public class GraphicsCanvas extends JPanel {

    public Graphics2D g2;

    public static final int default_tile_size = 16;

    public static final int basicFontSize  = 10;
    public static Font basicFont;


    int x0 = 0;
    int y0 = 0;
    float xzoom  = 0.05f;
    float yzoom  = 4.0f; 

    int curX;
    int curY;


    // ==== Transfromation between map [tile] and screen [pixel] coordinates

    public int tile_size;
    public int tile_size_half;

    public float      inv_tile_size;
    public int tiles_per_screen_x,tiles_per_screen_y;

    public int ix_min,ix_max,iy_min,iy_max;

    // ==== Settings for visualization of different things

    public BasicStroke route_stroke = new BasicStroke(3);
    public Color       route_color  = new Color( 10,10,200 ); 


    public GraphicsCanvas() {
            //map = new Map(true);
            //Graphics2D  g2;
            setBackground( new Color(43, 29, 15, 255) );
            setDoubleBuffered(true);
            setIgnoreRepaint(true);

            basicFont = new Font( "Verdana", Font.PLAIN, basicFontSize );
    }

    public final float screen2map_x( float x ){ return x/xzoom + x0; }
    public final float screen2map_y( float y ){ return y/yzoom + y0; }
    public final float map2screen_x( float x ){ return (x-x0)*xzoom; }
    public final float map2screen_y( float y ){ return (y-y0)*yzoom; }

    public void setCursor( int x, int y ) {
        curX = (int)screen2map_x(x);
        curY = (int)screen2map_y(y);
        //System.out.println( "cur " + curX+" "+curY );
    }    

    @Override
    public void paint( Graphics g ) {
        //Create Graphics2D object, cast g as a Graphics2D
        g2 = (Graphics2D) g;
        g2.setFont( basicFont ); 
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight() );

        paintNotes( Globals.composition.notes );

        //int i = (int) Globals.sequencer.getTickPosition();
        g2.setColor(Color.WHITE);
        int x = (int)map2screen_x(Globals.xcur);
        g2.drawLine( x, 0, x, 800);

        g2.setColor(Color.RED);
        g2.fillRect( (int)map2screen_x(curX),  (int)map2screen_y(curY), 10, (int)yzoom );

        //System.out.println( "x " + x );
    }
        

    public void paintNotes(Iterable<Note> notes){
        for(Note nt : notes){
            int x  = (int)map2screen_x(nt.start);
            int y  =  (int)map2screen_y(nt.key);
            int sx = (int)((nt.end-nt.start)*xzoom);
            int sy = (int)yzoom ;
            g2.setColor(Color.GRAY ); g2.fillRect( x, y, sx, sy );
            g2.setColor(Color.WHITE); g2.drawRect( x, y, sx, sy );
        }
    }

}
