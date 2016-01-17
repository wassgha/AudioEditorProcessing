/*

              Wassim Gharbi
     Audio Editing Software Project
                PAudacity
    _________________________________
    
    Drag and drop sounds to the timeline
    Use Right Click to modify a track
    Drag the slider to change instensity of effects
    
    
   

*/
import krister.Ess.*;
WorkCanvas mainWindow;
String[] soundfiles = new String[4];
DraggableObject[] draggables = new DraggableObject[soundfiles.length];
String [] buttonimages = new String[4];
Button [] buttons = new Button[4];
boolean pMousePressed=false;
int sumwidth=0, intensity=0;
Slider intensitySlider;

void setup() {
  size(1000, 550);
  mainWindow=new WorkCanvas(50, 100, width-100, 300, this);
  
  // Load button images for the GUI

  buttonimages[0]="icons/play.png";
  buttonimages[1]="icons/loop.png";
  buttonimages[2]="icons/delete.png";
  buttonimages[3]="icons/save.png";

  // Load sound files

  soundfiles[0]="you.wav";
  soundfiles[1]="the.wav";
  soundfiles[2]="great.wav";
  soundfiles[3]="rabbit.wav";
  
  // Create draggable objects using the sounds
  
  for(int i=0; i<soundfiles.length; i++) {
    // Create the draggable object and increase it's position everytime a new object is created
    draggables[i]=new DraggableObject(50+sumwidth, 470, soundfiles[i], this);
    sumwidth+=20+draggables[i].getWidth();
  }

  // Create the buttons for the GUI (play, loop, delete, etc.)

  for(int i=0; i<buttonimages.length; i++) {
    buttons[i]=new Button((width-30*buttonimages.length -50 )+i*30, 40, buttonimages[i], this);
  }
  
  // Create the slider object for controlling the intensity
  
  intensitySlider=new Slider(750, 480, this);
}
void draw() {
  
  background(71);
  
  textSize(34);
  text("Paudacity", 50, 60);
  textSize(13);
  text("by Wassim Gharbi", 210, 60);
  textSize(13);
  
  
  mainWindow.drawCanvas();
  mainWindow.drawSounds(intensitySlider.getValue());
  
  for(int i=0; i<buttonimages.length; i++) {
    buttons[i].draw();
  }
  
  
  fill(255);
  textAlign(LEFT);
  text("Sound Library : ", 50, 450);
  for(int i=0; i<soundfiles.length; i++) {
   draggables[i].draw(mainWindow);
  }
  
  text("Intensity of effects : ", 750, 450);
  intensitySlider.draw();
  

  pMousePressed=false;
}

void mousePressed() {
  if(buttons[0].mouseClick()) { // Play Button
    mainWindow.generateResult();
    mainWindow.getResult().play();
  } else if (buttons[1].mouseClick()) { // Loop Button
    mainWindow.generateResult();
    mainWindow.getResult().playLoop();
  } else if (buttons[2].mouseClick()) { // Clear Button
    mainWindow.clear();
  } else if (buttons[3].mouseClick()) { // Save Button
    selectOutput("Select a file to save to (add .wav)", "fileSelected");    
  }
  pMousePressed=true;
}

void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    mainWindow.save(selection.getAbsolutePath());
  }
}