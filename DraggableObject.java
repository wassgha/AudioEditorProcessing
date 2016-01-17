import processing.core.*;

/*

         Draggable Object Class
    _________________________________
    
    Creates a draggable button for a
    sound. When clicked, the button plays
    the sound and when dragged and dropped
    on the timeline, it adds the sound
    to the work canvas.
    
   

*/

public class DraggableObject {
    int x,y, initx, inity, width;
    String name;
    PApplet p;
    WSound sound;
    String file;
    boolean dragging=false;
    boolean pMousePressed=false;
   DraggableObject(int x, int y,  String file, PApplet p) {
     this.x=x;
     this.y=y;
     this.initx=x;
     this.inity=y;
     this.file=file;
     this.sound=new WSound(file,p);
     this.p=p;
     // The draggable object's label is just the files name capitalized and cleaned
     this.name=file.replaceAll("_"," ").replaceAll(".wav","").trim();
     this.name=this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
     this.width=(int)p.textWidth(this.name)+50;
   }
   
   public void draw(WorkCanvas mainWindow) {
     p.noStroke();
     if(mouseOver()||dragging) {
       p.fill(100,100,100,170);
     } else {
       p.fill(46);
     }
     p.rect(x, y, width, 30);
     p.fill(255);
     p.textAlign(p.LEFT);
     p.triangle(x+10, y+10, x+10+10, y+15, x+10, y-10+30);
     p.text(name, x+35, y+20);
     if((!pMousePressed && p.mousePressed && mouseOver())||dragging) {
       sound.play();
       x-=p.pmouseX-p.mouseX;
       y-=p.pmouseY-p.mouseY;
       dragging=true;
     }
     if(!p.mousePressed && dragging) {
       if(mainWindow.addindexAtXY(p.pmouseX,p.mouseY) != -1) {
           mainWindow.addSoundAt(sound, mainWindow.addindexAtXY(p.pmouseX,p.mouseY));
       }
       dragging=false;
       pMousePressed=false;
       x=initx;
       y=inity;
     }
     if(!p.mousePressed) {
       dragging=false;
       pMousePressed=false;
     }
     if(p.mousePressed && !pMousePressed) {
       pMousePressed=true;
     }
     
     
   }
   
   public boolean mouseOver() {
     return p.mouseX > x && p.mouseX < x + width && p.mouseY>y && p.mouseY<y+30;
   }
  
  // Returns the width of the button
  
   public int getWidth() {
     return this.width;
   }
   
}
