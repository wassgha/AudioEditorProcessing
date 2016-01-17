import processing.core.*;

/*

              Button Class
    _________________________________
    
    Used to create and display buttons
    using an image file and provide
    feedback on whether the button is 
    clicked or not
    
   

*/

public class Button {
  private PApplet p;
  private PImage image;
  private int x,y;
  Button(int x, int y, String imagefile, PApplet p) {
    this.p=p;
    this.image=p.loadImage(imagefile);
    this.x=x;
    this.y=y;

  }
  
  // draws the button
  public void draw() {
     p.image(image, x,y);
  }
  
  // if the mouse if over the button
  public boolean mouseOver() {
     return p.mouseX > x && p.mouseX < x + image.width && p.mouseY>y && p.mouseY<y+image.height;
  }
  
  // if the button is clicked
  public boolean mouseClick() {
     return p.mousePressed && mouseOver();
  }
      
}