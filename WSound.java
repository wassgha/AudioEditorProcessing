import krister.Ess.*;
import processing.core.*;

/*

         WSound Class
    _________________________________
    
    Extends the Ess library by providing
    more simple functions to deal with 
    sound :
    * Remove Silence using a threshold
    * Add Silence
    * Draw a sound wave
    * Normalize a sound ( change volume)
    * Concatenate two sounds
    * Save a sound to a file
    
   

*/

public class WSound {
  private AudioChannel channel;
  private PApplet p;
  private String file;
  // Constructor to create a WSound out of a file name
  public WSound(String file, PApplet p) {
      Ess.start(p);
      this.p=p;
      this.file=file;
      this.channel=new AudioChannel(file);
  }
  // Constructor to create an empty WSound of size size
  public WSound(int size, PApplet p) {
      Ess.start(p);
      this.p=p;
      this.channel=new AudioChannel(size);
  }
  // Constructor to create a WSound out of another WSound
  public WSound(WSound sound, PApplet p) {
      Ess.start(p);
      this.p=p;
      this.channel=new AudioChannel(sound.channel.size,sound.channel.sampleRate);
      p.arrayCopy(sound.channel.samples, this.channel.samples);
  }
  
  public void play() {
    if(channel.size!=0)
      channel.play();
  }
  
  public void playLoop() {
    if(channel.size!=0)
      channel.play(Ess.FOREVER);
  }
  
  // Saves the sound to a file
   public void save(String filename) {
    if(channel.size!=0)
      channel.saveSound(filename);
  }
 
  public float maxAmplitude(){
    return maximumAbs(channel.samples);
  }
  
  // Finds the maximum ( or absolute minimum whichever is bigger) of array values

  public float maximumAbs(float [] array){
    float maximum=array[0];
    for ( int i=1; i<array.length; i++) { 
      if(p.abs(array[i])>maximum)
        maximum=p.abs(array[i]);
    }
    return maximum;
  }  
  
  // Finds the position of the first value above a threshold
  
  private int findfirst(float[] array, float threshold) {
    for(int i=0; i<array.length; i++) 
       if(p.abs(array[i])>threshold)
         return i;
    return 0;
  }

  // Finds the position of the last value above a threshold

  private int findlast(float[] array, float threshold) {
    for(int i=0; i<array.length; i++) 
       if(p.abs(array[array.length-i-1])>threshold)
         return i;
    return 0;
  }
  

  // Draws the wave

  public void draw(int x, int y, int w, int h, int c) {
    p.stroke(c);
    
    float scale_x=w/((float)(channel.samples.length-1)); //width of the canvas divided by number of data points gives the horizontal scale
    float scale_y=h/2; //height of the canvas divided by vertical range gives the vertical scale
    
    for(int i=0; i<channel.size-1; i+=p.floor((float)p.width/w)) { 
      // y+h is y position of the bottom edge of the canvas, we're drawing in reverse
      p.line(i*scale_x+x, (y+h)-(channel.samples[i]+1)*scale_y,  (i+1)*scale_x+x, (y+h)-(channel.samples[i+1]+1)*scale_y);
    }
  }
  
  // Elevates all amplitudes to a value proportional to the maximum given
  public void normalize(float maximum) {
    if (maximum<0 || maximum >1) {
      maximum=1;
      p.println("Maximum level out of bounds, setting to 1");
    }
    //Multiplier is the intensity divided by the maximum or the minimum whichever is bigger
    float multiplier=maximum/maxAmplitude();
    for (int i=0; i<channel.size; i++ ) {
      channel.samples[i]*=multiplier;
    }
  }
  
  // Removes silence (silence is all sound of amplitude lower than the given threshold)
  public void trim(float threshold) {
    if(threshold>0 && threshold < maxAmplitude()) {
       // adjustChannel adds and removes samples to the sound
      channel.adjustChannel(-findfirst(channel.samples, threshold),Ess.BEGINNING);
      channel.adjustChannel(-findlast(channel.samples, threshold),Ess.END);
    } else {
      p.println("Threshold is larger than the maximum amplitude");
      channel.initChannel(1);
    }
  }
  
  // Adds a silence of duration time at the beginning or the end
  public void addSilence(float time, boolean beginning) {
    int framestoadd=(int)(time*channel.sampleRate); // calculate number of samples to add
    float[] newsamples= new float[channel.size+framestoadd]; // create the new sound with added size
    for(int i=0; i<channel.size+framestoadd; i++) {
      // loop through the old sound and either add empty frames at the beginning or at the end
      if(beginning) {
        if(i>framestoadd) {
          newsamples[i]=channel.samples[i-framestoadd];
        } else {
           newsamples[i]=0;
        }
      } else {
        if(i>channel.size-1) {
          newsamples[i]=0;
        } else {
          newsamples[i]=channel.samples[i];
        }
      }
    }
    channel.initChannel(channel.size+framestoadd);
    channel.samples=newsamples;
  }
  
  //Concatenate two WSounds
  public WSound concat(WSound sound2) {
    WSound result=new WSound(channel.size+sound2.channel.size, p); //Create a new WSound with size equal to the sum of both sizes
     // concatenate the first samples array with the second one and put them in the new WSound's samples array
     // could've looped through the result samples array and added both arrays one sample at a time but the "concat" function is way cleaner
    result.channel.samples=p.concat(channel.samples, sound2.channel.samples);
    result.channel.sampleRate(sound2.channel.sampleRate);
    return result;
  }
  
  public int getDuration() {
    return channel.duration;
  }
  
  public String getFileName() {
    return file;
  }
  
  public boolean getState() {
    return channel.state==Ess.PLAYING;
  }
  
}