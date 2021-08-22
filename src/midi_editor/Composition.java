
package midi_editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Composition {

    int nTracks  = 1;
    //division     = 16;
    int resolution   = 16;
    
    //int tempo = 100;
    //HashMap<Integer,Note> notes;
    ArrayList<Note> notes;
    //ArrayList<HashMap<Integer,Note>> notes;
    //Sequence                         sequence;
    
    int curChan; // active channel
    
    Composition(){
        notes = new ArrayList();
    }
    
    //void loadMIDI(String fname) throws InvalidMidiDataException, IOException{
    //    Sequence sequence = MidiSystem.getSequence( new File(fname) );
    //    fromSequence( sequence );
    //}
    
    void fromSequence( Sequence sequence ){
        int trackNumber = 0;
        
        resolution = sequence.getResolution();
                
        //System.out.println( "sequence.getResolution()   "+sequence.getResolution() );
        //System.out.println( "sequence.getDivisionType() "+sequence.getDivisionType()  );
        
        HashMap<Integer,Note> active;
        active = new HashMap();
        
        //notes = new ArrayList<HashMap<Integer,Note>>( sequence.getTracks().length );
        for (Track track :  sequence.getTracks() ){
            //HashMap<Integer,Note> track_notes = new HashMap<Integer,Note>();
            trackNumber++;
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                long tick = event.getTick();
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    int chan = sm.getChannel();
                    if (sm.getCommand() == Globals.NOTE_ON) {
                        int key      = sm.getData1();
                        int velocity = sm.getData2();
                        Note nt      = new Note( key,velocity, tick,-1, trackNumber );
                        active.put(key, nt);
                    } else if (sm.getCommand() == Globals.NOTE_OFF) {
                        int key      = sm.getData1();
                        Note nt = active.get(key);
                        if(nt != null ){
                            nt.end = tick;
                            //System.out.println( "nt "+nt );
                            notes.add( nt );
                            active.remove(key);
                        }
                    }// else {
                }// else {
            }
        }
    }
    
    Sequence toSequence() throws InvalidMidiDataException{
        //                  Sequence(float divisionType, int resolution, int numTracks);
        Sequence seq = new Sequence( Sequence.PPQ, resolution, nTracks);
        //Sequence seq = new Sequence( 0.1f, resolution, nTracks);
        Track track = seq.createTrack();
        

        
        int channel = 2;
                
        for(int i=0; i<notes.size(); i++){
            Note nt = notes.get(i);
            
            ShortMessage start = new ShortMessage();
            start.setMessage(ShortMessage.NOTE_ON, channel, nt.key, nt.velocity);
            track.add( new MidiEvent( start, nt.start) );
            
            ShortMessage end = new ShortMessage();
            end.setMessage(ShortMessage.NOTE_OFF, channel, nt.key, nt.velocity);
            track.add( new MidiEvent( end, nt.end) );
        }
        
        
        /*
        for (int i=0; i < track.size(); i++) { 
            MidiEvent event = track.get(i);
            long tick = event.getTick();
            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;
                int chan = sm.getChannel();
                System.out.println( "["+tick+"] "+sm.getCommand()+" "+ sm.getData1()+" "+ sm.getData2() );
                
            }// else {
        }
        */
        return seq;
    }
    
    int findNote( int tick, int key ){
        for(int i=0; i<notes.size(); i++){
            Note nt = notes.get(i);
            if( nt.cursorIn(key, tick) ) return i;
        }
        return -1;
    }
    
    void addNote( int key, int velocity,  int start, int end, int track ){
        Note nt      = new Note( key, velocity, start, end, track );
        System.out.println( "addNote "+nt );
        notes.add(nt);
        
    }
    
    void eraseNote( int tick, int key  ){
        int i = findNote( tick, key );
        System.out.println( "eraseNote "+i+" | "+tick+" "+key );
        if( i>=0 ){
            notes.remove(i);
        }
    }
    
    void print(){
        for(int i=0; i<notes.size(); i++){
            System.out.println("["+i+"] " + notes.get(i) );
        }
    }
    
}
