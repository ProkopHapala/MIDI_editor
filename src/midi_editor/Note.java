/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi_editor;

public class Note {
    int key;
    int velocity;
    int track;
    long start;
    long end;
    //int octave; = (key / 12)-1;
    //int note = key % 12;
    
    Note(int key_, int velocity_, long start_, long end_, int track_){ key=key_; velocity=velocity_; track=track_; start=start_; end=end_; };
    Note(){};
    
    boolean cursorIn( int key_, int tick ){
        return (key==key_)&&(tick>start)&&(tick<end);
    }
    
    @Override
    public String toString(){ 
        return "("+start+" "+end+" ) "+key+" "+velocity+" "+track; 
    };
}
