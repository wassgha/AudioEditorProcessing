import processing.core.*;

/*

         Contextual Menu Class
    _________________________________
    
    Used to create and display a 
    contextual menu when right clicking
    on a sound on the timeline.
    Applies the chosen effect on the
    sound.
    
   

*/

public class ContextualMenu {
  private PApplet p;
  private WorkCanvas mainWindow;
  private int x,y, w, h;
  private String [] menutext = {"Change volume", "Remove silence", "Add silence to the beginning" , "Add silence to the end", "Delete"};
  
  
  ContextualMenu(PApplet p, WorkCanvas mainWindow) {
    this.p=p;
    this.mainWindow=mainWindow;
  }
  
  
  public void draw(int x, int y, int intensity) {
    this.x=x;
    this.y=y;
    p.textAlign(p.LEFT);
    for (int i=0; i<menutext.length; i++) {
      if(p.mouseX>x && p.mouseX<x+200 && p.mouseY> y+i*30 && p.mouseY<y+i*30+30) {
        p.fill(91);
        if (p.mousePressed) {
          switch (i) {
            case 0 : // Normalize sound
              mainWindow.getSoundAt(mainWindow.indexAtXY(x,y)).normalize((float)p.map(intensity, 0, 30, 0, 1));
              mainWindow.closeContextualMenu();
              break;
            case 1 : // Remove Silence
              mainWindow.getSoundAt(mainWindow.indexAtXY(x,y)).trim((float)p.map(intensity, 0, 30, 0, mainWindow.getSoundAt(mainWindow.indexAtXY(x,y)).maxAmplitude()));
              mainWindow.closeContextualMenu();
              break;
            case 2 : // Add Silence to the beginning
              mainWindow.getSoundAt(mainWindow.indexAtXY(x,y)).addSilence((float)p.map(intensity, 0, 30, 0, 1), true);
              mainWindow.closeContextualMenu();
              break;
            case 3 : // Add Silence to the end
              mainWindow.getSoundAt(mainWindow.indexAtXY(x,y)).addSilence((float)p.map(intensity, 0, 30, 0, 1), false);
              mainWindow.closeContextualMenu();
              break;
            case 4 : // Delete sound from timeline
              mainWindow.delSoundAt(mainWindow.indexAtXY(x,y));
              mainWindow.closeContextualMenu();
              break;
          }
          
        }
      } else {
        p.fill(80);
      }
      p.stroke(100);
      p.rect(x, y+i*30, 200,30);
      p.fill(200);
      p.text(menutext[i], x+10, y+i*30 +20);
    }
  }
  
  
  public boolean mouseOver() {
    return p.mouseX>x && p.mouseX<x+200 && p.mouseY>y && p.mouseY<y+200;
  }
}
