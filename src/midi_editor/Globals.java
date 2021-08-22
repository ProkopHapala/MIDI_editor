/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi_editor;

import javax.sound.midi.Sequencer;

public class Globals {
    static Composition composition;
    static Sequencer   sequencer;
    static int         xcur;
    
    public static final int  NOTE_ON = 0x90;
    public static final int  NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    
}
