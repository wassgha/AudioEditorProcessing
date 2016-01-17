import processing.core.*;

/*

         Slider Object Class
    _________________________________
    
    Creates a slider object, draws it 
    and when needed, return its current
    value.
    
   

*/

public class Slider {
  private PApplet p;
  private int x,y,value=0;
  private boolean dragging;
  Slider(int x, int y, PApplet p) {
    this.p=p;
    this.x=x;
    this.y=y;

  }
  public void draw() {
    p.rect(x, y, 200, 10, 4, 4, 4, 4);
    p.fill(180);
    p.noStroke();
    p.ellipse(x + value, y+5, 20,20);
    // If the slider is getting dragged or clicked then change its value
    if (dragging || mouseClick()) {
        value=p.constrain(value+(p.mouseX-p.pmouseX), 0, 200); 
        dragging=true;
    }
    // When the mouse is released, set dragging to false
    if(!p.mousePressed) {
      dragging=false;
    }
  }
  
  public int getValue() {
    return (int)p.map(value, 0, 200, 1, 30);
  }
  
  //Checks if the slider is clicked
  public boolean mouseClick() {
     return (p.mousePressed && p.mouseX>(x+value-10) && p.mouseX<(x + value +10) && p.mouseY>y+5-10 && p.mouseY<y+5+10);
  }
      
}