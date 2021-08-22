/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi_editor;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author prokop
 */
public class MIDI_editor extends JFrame implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
    
    public static GraphicsCanvas    canvas;
    Composition comp;
    Sequencer sequencer;
    Sequence  sequence;
    int iframe=0;
    
    
    int drawVelocity = 60;
    int curTrack     = 2;
    int drawTick;     
    int drawKey;      
    boolean bNoteStarted = false;

    
    public static void main(String[] args) throws Exception {
            //System.setOut(outputFile("output.log"));
            MIDI_editor instance = new MIDI_editor();
            //instance.start();
            instance.run();
    }
    
    public MIDI_editor() throws InvalidMidiDataException, IOException, MidiUnavailableException{
        
        this.setFocusTraversalKeysEnabled(false);
        //setTitle( INFO );
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit( 0 );
            }
        });
        setSize( 1600, 800 );
        show();
        
        this.addKeyListener         ( this );
        this.addMouseListener       ( this );
        this.addMouseMotionListener ( this );
        this.addMouseWheelListener  ( this );
        
        canvas     = new GraphicsCanvas();		
		
        this.getContentPane().add   (canvas);
        
        //String fname = "/home/prokop/SW/Fun-Programming-master/processing/ideas/2017/04/MidiViz/data/pianocon.mid";
        String fname = "/home/prokop/lmms/projects/MIDI/Vyber_Simon_/Let-frag1.MID";
        comp = new Composition();
        //comp.loadMIDI(fname);
        sequence = MidiSystem.getSequence( new File(fname) );
        comp.fromSequence( sequence );
        Globals.composition = comp;
  
        sequencer = MidiSystem.getSequencer(); // Get the default Sequencer
        if (sequencer==null) {
            System.out.println("Sequencer device not supported");
            return;
        }
        sequencer.open(); // Open device
        
        
        Track track = sequence.createTrack();
        for (int i = 0; i < 10000; i++){
            ShortMessage msg = new ShortMessage();
            msg.setMessage(176, 0, 110, 0);
            long tickPosition = i * 25;
            MidiEvent me      = new MidiEvent(msg, tickPosition);
            track.add(me);
        }

        int[] controllers = new int[]{ 110 };
        sequencer.addControllerEventListener(shortMessage -> updateSlider(shortMessage), controllers);

        //Sequence sequence = MidiSystem.getSequence(new File( "/home/prokop/lmms/projects/MIDI/Vyber_Simon_/Let-frag1.MID"));
        //sequencer.setSequence(sequence); // load it into sequencer            
        sequencer.setSequence(sequence); // load it into sequencer
        sequencer.start();
        
        this.toFront();
	this.requestFocus(); 
        
    }
    
    public void run() {	
        //System.out.println( "mouseMoved " );
        int frameDelay      =  10;                             // [ milisecond ]
        int lazyUpdateDelay =  500;                            // [ milisecond ]
        int framesPerLazy   =  lazyUpdateDelay / frameDelay ;  // [ 1 ]
        while (true) {
            iframe ++;
            canvas.repaint();
            //if( ( iframe % framesPerLazy ) == 0 ) {
            //        GameUI.update( );
            //}
            //this.update( canvas.g2 );
            try{ Thread.sleep( 10 ); }catch (Exception e){};
            //break;
        }
    }
    
    

    public void saveMidiFile(File file) {
        try {
            int[] fileTypes = MidiSystem.getMidiFileTypes(sequence);
            if (fileTypes.length == 0) {
                System.out.println("Can't save sequence");
            } else {
                if (MidiSystem.write(sequence, fileTypes[0], file) == -1) {
                    throw new IOException("Problems writing to file");
                }
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Process event to update the slider on the Swing Event Dispatching Thread
    void updateSlider(ShortMessage sm){
        // Convert tick position to slider value
        //Globals.xcur = (int)( canvas.WIDTH * sequencer.getTickPosition() / (double) sequencer.getTickLength() );
        Globals.xcur = (int)  sequencer.getTickPosition();
        //System.out.println( "Globals.xcur "+ Globals.xcur );
        //int sliderPosition = (int) (slider.getMinimum() + (slider.getMaximum() - slider.getMinimum()) * percentage);
        //SwingUtilities.invokeLater(() -> slider.setValue(sliderPosition));
    }
    
   
    @Override
    public void keyTyped(KeyEvent ke) {
        //System.out.println( "mouseMoved " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if( ke.getKeyChar() == 's' ){
            Sequence seq;
            try {
                System.out.println( "Save sequence " );
                seq = comp.toSequence();
                sequencer.stop();
                sequencer.setSequence(seq); // load it into sequencer
                sequencer.start();
                File file = new File("/home/prokop/lmms/projects/MIDI/Vyber_Simon_/Let-frag1-mod.MID");
                saveMidiFile( file );
            } catch (InvalidMidiDataException ex) {
                //Logger.getLogger(MIDI_editor.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println( ex );
            }
            

            
        }
        //System.out.println( "mouseMoved " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //System.out.println( "mouseMoved " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        //System.out.println( "mouseMoved " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int mbt = me.getButton();
        if ( mbt == MouseEvent.BUTTON1){
            Point pos = canvas.getParent().getMousePosition();
            drawTick  = (int)canvas.screen2map_x(pos.x);
            drawKey   = (int)canvas.screen2map_y(pos.y);
            bNoteStarted = true;
        }else if (mbt == MouseEvent.BUTTON3 ){
            Point pos = canvas.getParent().getMousePosition();
            int tick = (int)canvas.screen2map_x(pos.x);
            int key  = (int)canvas.screen2map_y(pos.y);
            comp.eraseNote(tick,key);
        }
        //System.out.println( " mousePressed " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if( bNoteStarted){
            Point pos = canvas.getParent().getMousePosition();
            int tick  = (int)canvas.screen2map_x(pos.x);
            comp.addNote( drawKey, drawVelocity, drawTick, tick, curTrack  );
            bNoteStarted = false;
        }
        //System.out.println( "mouseReleased " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //System.out.println( "mouseMoved " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //System.out.println( "mouseMoved " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        //System.out.println( "mouseMoved " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        //System.out.println( "mouseMoved " );
        Point pos = canvas.getParent().getMousePosition();
        canvas.setCursor(pos.x, pos.y);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        //System.out.println( "mouseMoved " );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}

