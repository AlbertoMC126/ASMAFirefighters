import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

/** class that implements the Model of Domestic Robot application */
public class HouseModel extends GridWorldModel {
  
  // constants for the grid objects
  public static final int FRIDGE = 16;
  public static final int OWNER  = 32;
  public static final int FUEGO_DEBIL = 1;
  public static final int FUEGO_VIVO = 2;

  // the grid size
  public static final int GSize = 10;
  
  boolean fridgeOpen   = false; // whether the fridge is open
  boolean carryingBeer = false; // whether the robot is carrying beer
  int sipCount     = 0; // how many sip the owner did
  int availableBeers = 2; // how many beers are available

  Location lFridge = new Location(0,0);
  Location lOwner  = new Location(GSize-1,GSize-1);
  Location [] list_lFuegoDebil = new Location[8];

  public HouseModel() {  
    // create a 7x7 grid with one mobile agent
    super(GSize, GSize, 2);

    // initial location of robot (column 3, line 3)
    // ag code 0 means the robot
    setAgPos(0, GSize/2, GSize/2);
    setAgPos(1,1,0);
    System.out.println("Number of agents: "+getNbOfAgs());
    
    list_lFuegoDebil[0] = new Location(4,4);
    list_lFuegoDebil[1] = new Location(4,5);
    list_lFuegoDebil[2] = new Location(4,6);
    list_lFuegoDebil[3] = new Location(5,4);
    list_lFuegoDebil[4] = new Location(5,6);
    list_lFuegoDebil[5] = new Location(6,4);
    list_lFuegoDebil[6] = new Location(6,5);
    list_lFuegoDebil[7] = new Location(6,6);
    // initial location of fridge and owner
    add(FRIDGE, lFridge);
    add(OWNER, lOwner);
    add(FUEGO_DEBIL, list_lFuegoDebil[0]);
    add(FUEGO_DEBIL, list_lFuegoDebil[1]);
    add(FUEGO_DEBIL, list_lFuegoDebil[2]);
    add(FUEGO_DEBIL, list_lFuegoDebil[3]);
    add(FUEGO_DEBIL, list_lFuegoDebil[4]);
    add(FUEGO_DEBIL, list_lFuegoDebil[5]);
    add(FUEGO_DEBIL, list_lFuegoDebil[6]);
    add(FUEGO_DEBIL, list_lFuegoDebil[7]);
    System.out.println("Number of Light fires: "+countObjects(FUEGO_DEBIL));
  }

  boolean openFridge() {
    if (!fridgeOpen) {
      fridgeOpen = true;
      return true;
    } else {
      return false;
    }
  }

  boolean closeFridge() {
    if (fridgeOpen) {
      fridgeOpen = false; 
      return true;
    } else {
      return false;
    }        
  }  

  boolean moveTowards(int direccion) {
    Location r1 = getAgPos(0);
	if(direccion == 0){ //Mover arriba
		if(r1.x-1 >=0){
			r1.x--;
		}
	}else if(direccion == 1){//Diagonal derecha arriba
		if(r1.x-1 >=0){
			r1.x--;
		}
		if(r1.y+1<GSize){
			r1.y++;
		}
	}else if(direccion == 2){//Derecha
		if(r1.y+1<GSize){
			r1.y++;
		}
	}else if(direccion == 3){//Diagonal derecha abajo
		if(r1.x+1<GSize){
			r1.x++;
		}
		if(r1.y+1<GSize){
			r1.y++;
		}
	}else if(direccion == 4){//Abajo
		if(r1.x+1<GSize){
			r1.x++;
		}
	}else if(direccion == 5){//Diagonal izquierda abajo
		if(r1.x+1<GSize){
			r1.x++;
		}
		if(r1.y-1 >=0){
			r1.y--;
		}
	}else if(direccion == 6){//Izquierda
		if(r1.y-1 >=0){
			r1.y--;
		}
	}else if(direccion == 7){//Diagonal izquierda arriba
		if(r1.x+1<GSize){
			r1.x++;
		}
		if(r1.y-1 >=0){
			r1.y--;
		}
	}else{
		//Do nothing m8, should not be here DUDE
	}
    setAgPos(0, r1); // move the robot in the grid
        
    // repaint the fridge and owner locations
    view.update(lFridge.x,lFridge.y);
    view.update(lOwner.x,lOwner.y);
    return true;
  }
  
  boolean eval_loc(){
	  //Siempre consigo evaluar la casilla
	  return true;
  }
  
  boolean getBeer() {
    if (fridgeOpen && availableBeers > 0 && !carryingBeer) {
      availableBeers--;
      carryingBeer = true;
      view.update(lFridge.x,lFridge.y);
      return true;
    } else {
      return false;
    }
  }
  
  boolean addBeer(int n) {
    availableBeers += n;
    view.update(lFridge.x,lFridge.y);
    return true;
  }
  
  boolean handInBeer() {
    if (carryingBeer) {
      sipCount = 10;
      carryingBeer = false;
      view.update(lOwner.x,lOwner.y);
      return true;
    } else {
      return false;
    }
  }
  
  boolean sipBeer() {
    if (sipCount > 0) {
      sipCount--;
      view.update(lOwner.x,lOwner.y);
      return true;
    } else {
      return false;
    }
  }
  
  boolean wander(){
	Location r1 = getAgPos(0);
	int mov_x = (int)Math.round(Math.random());
	int mov_y = (int)Math.round(Math.random());
	r1.x = r1.x + mov_x;
	r1.y = r1.y + mov_y;
	setAgPos(0, r1); // move robot randomly
	    
	// repaint the fridge and owner locations
	view.update(lFridge.x,lFridge.y);
	view.update(lOwner.x,lOwner.y);
	return true;
  }
}
