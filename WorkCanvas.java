import processing.core.*;

/*

         Work Canvas Class
    _________________________________
    
    Provides a timeline view of sounds,
    stores WSound objects and manages
    them by adding, deleting, moving
    and editing the sound snippets to
    finally concatenate them and create
    a track that can be saved to a wav
    file.
    
   

*/


public class WorkCanvas {
  private int windowwidth, windowheight, windowx, windowy, numLabels, contextmenux, contextmenuy;
  private WSound[] sounds = new WSound[100];
  private int soundcount=0;
  private PApplet p;
  private ContextualMenu contextmenu;
  private boolean contextmenuopened=false;
  private WSound result;
  
  // Construct a new Canvas
  WorkCanvas(int x, int y, int w, int h, PApplet p) {
    this.windowx=x;
    this.windowy=y;
    this.windowwidth=w;
    this.windowheight=h;
    this.numLabels=4;
    this.p=p;
    this.result=new WSound(0,p);
    this.contextmenu=new ContextualMenu(p, this);
  }
  
  // Adds a sound to the work area (stores it as a new WSound)
  public void addSound(WSound sound) {
    sounds[soundcount]=new WSound(sound, p);
    soundcount++;
  }
  
  // Adds a sound at a specific position (after or before another sound)
  public void addSoundAt(WSound sound, int index) {
    sounds=(WSound[])p.splice(sounds, new WSound(sound, p), index);
    soundcount++;
  }
  
  // Delete a sound from the timeline
  public void delSoundAt(int index) {
    // split the array of sounds to two parts, one before the sound to delete and one after it then contatenates both
    // parts eliminating the sound to delete
    p.arrayCopy(sounds, index+1, sounds, index, sounds.length-index-1);
    sounds=(WSound[])p.shorten(sounds);
    soundcount--;
  }
  
  //get the sound at an index
  public WSound getSoundAt(int index) {
    return sounds[index];
  }
  
  public void generateResult() {
    result= new WSound(0, p);
    for (int i=0; i<soundcount; i++) {
      result=result.concat(sounds[i]);
    }
  }
  
  public WSound getResult() {
    return result;
  }
  
  public void clear () {
     sounds = new WSound[100];
     soundcount=0;
  }
  
  public void save (String filename) {
    result.save(filename);
  }
  
  public void drawCanvas() {
    p.fill(56);
    p.noStroke();
    p.rect(windowx, windowy, windowwidth, windowheight);
    p.fill(46);
    p.rect(windowx, windowy, 50, windowheight);
    p.stroke(255);
    p.fill(255);
    p.textAlign(p.CENTER);
    for(int i=0; i<numLabels+1; i++) {
      p.text("" + p.nfc(1-((float)(2*i)/numLabels),1), windowx+25, windowy+25+i*(windowheight/numLabels-10));
    }

  }
  public void drawSounds(int intensity) {
    
    // if there are no sounds, displays a message
    if(soundcount==0) {
          p.textAlign(p.CENTER);
          p.text("Drag and drop a sound from the Sound Library to start", windowx+windowwidth/2, windowy+windowheight/2);
    }
    
    for (int i=0; i<soundcount; i++) {
      if(indexAtXY(p.mouseX,p.mouseY)==i) {
        // Draws a black rectangle before the wave to highlight the selected sound
        p.fill(0,0,0,30);
        p.noStroke();
        p.rect(windowx+50+i*((windowwidth-50)/soundcount), windowy, (windowwidth-50)/soundcount, windowheight);
      }
      sounds[i].draw(windowx+50+i*((windowwidth-50)/soundcount), windowy, (windowwidth-50)/soundcount, windowheight, p.color(223,57,91));
    }
    if (soundcount !=0 && p.mousePressed & p.mouseButton==p.RIGHT && indexAtXY(p.mouseX,p.mouseY)!=-1) {
      // opens the contextual menu
      contextmenuopened=true;
      contextmenux=p.mouseX;
      contextmenuy=p.mouseY;
    } else if (p.mousePressed && !contextmenu.mouseOver()) {
      // closes the contextual menu
      contextmenuopened=false;
    } else if (soundcount ==0) {
      // closes the contextual menu when all sounds are deleted
      contextmenuopened=false;
    }
    
    // Draw the contextual menu
    if(contextmenuopened) {
      contextmenu.draw(contextmenux,contextmenuy, intensity);
    }
  }
  
  // hide the contextual menu
  public void closeContextualMenu() {
     contextmenuopened=false;
  }

  // Returns the position of the sound at the specific coordinates (used to know which sound to edit when the contextual menu is opened)
  public int indexAtXY(int x, int y) {
    if(x>=windowx+50 && x<windowx+windowwidth && y>=windowy && y<=windowy+windowheight) {
      if(soundcount==0)
        return 0;
      int xinside=x-windowx-50;
      int yinside=y-windowy;
      return (xinside)/((windowwidth-50)/(soundcount));
    }
    return -1;
  }
  
  // Returns the position at which to add a sound (Ex: with only one sound in the timeline, if the mouse is past half the width of the timeline
  // then this returns 1 (you should add at position 1, after the current sound) and if the mouse is on the first half of the timeline then it 
  // return 0 ( add before the current sound)
  public int addindexAtXY(int x, int y) {
    if(x>=windowx+50 && x<windowx+windowwidth && y>=windowy && y<=windowy+windowheight) {
      if(soundcount==0)
        return 0;
      int xinside=x-windowx-50;
      int yinside=y-windowy;
      return (xinside)/((windowwidth-50)/(soundcount+1));
    }
    return -1;
  }
}