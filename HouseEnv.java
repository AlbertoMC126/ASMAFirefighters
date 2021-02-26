import jason.NoValueException;
import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;
import java.util.Random;

public class HouseEnv extends Environment {
  //ESTO ES UN COMMENTARIO
  // common literals
  public static final Literal of  = Literal.parseLiteral("open(fridge)");
  public static final Literal clf = Literal.parseLiteral("close(fridge)");
  public static final Literal gb  = Literal.parseLiteral("get(beer)");
  public static final Literal hb  = Literal.parseLiteral("hand_in(beer)");
  public static final Literal sb  = Literal.parseLiteral("sip(beer)");
  public static final Literal hob = Literal.parseLiteral("has(owner,beer)");

  public static final Literal af = Literal.parseLiteral("at(robot,fridge)");
  public static final Literal ao = Literal.parseLiteral("at(robot,owner)");
  
  //Extras
  public static final Literal wd = Literal.parseLiteral("wander()");
  public static final Literal atloc = Literal.parseLiteral("at(robot,location)");
  public static final Literal evaloc = Literal.parseLiteral("evaluate(location)");
  public static final Literal awfr = Literal.parseLiteral("at(robot,weakfire)");

  HouseModel model; // the model of the grid

  @Override
  public void init(String[] args) {
    model = new HouseModel();
    
    if (args.length == 1 && args[0].equals("gui")) { 
      HouseView view  = new HouseView(model);
      model.setView(view);
    }
    
    updatePercepts();
  }
    
  /** creates the agents percepts based on the HouseModel */
  void updatePercepts() {
    // clear the percepts of the agents
    clearPercepts("robot");
    clearPercepts("owner");
    
    // get the robot location
    Location lRobot = model.getAgPos(0);
    //get location robot2
    Location lRobot_2 = model.getAgPos(1);
    
    //add agent location to ITS percepts
    if (lRobot_2.equals(model.lFridge)) {
        addPercept("robot", af);
      }
      if (lRobot_2.equals(model.lOwner)) {
        addPercept("robot", ao);
      }
    
    // add agent location to its percepts
    if (lRobot.equals(model.lFridge)) {
      addPercept("robot", af);
    }
    if (lRobot.equals(model.lOwner)) {
      addPercept("robot", ao);
    }
    
    // add beer "status" to the percepts
    if (model.fridgeOpen) {
      addPercept("robot", Literal.parseLiteral("stock(beer,"+model.availableBeers+")"));
    }
    if (model.sipCount > 0) {
      addPercept("robot", hob);
      addPercept("owner", hob);
    }
    
    for(int k = 0;k<model.list_lFuegoDebil.length;k++){
    	if(model.getAgPos(0) == model.list_lFuegoDebil[k]){
    		System.out.println("----------------------------------------------------------------");
    		System.out.println("---------------ADDING PERCEPTION OF FIRE-------------------");
    		System.out.println("----------------------------------------------------------------");
    		addPercept("robot", af);
    	}
    }
  }

  @Override
  public boolean executeAction(String ag, Structure action) {
    System.out.println("["+ag+"] doing: "+action);
    boolean result = false;
    if (action.equals(of)) { // of = open(fridge)
      result = model.openFridge();
      
    } else if (action.equals(clf)) { // clf = close(fridge)
      result = model.closeFridge();
      
    } else if (action.getFunctor().equals("move_towards")) {
    	
      String l = action.getTerm(0).toString();
      int direccion = 0;
      if (l.equals("location")) {
    	  Random rand = new Random();
        direccion = rand.nextInt(8);
      }
      

      try {
	result = model.moveTowards(direccion);
      } catch (Exception e) {
        e.printStackTrace();
      }
            
    } else if (action.getFunctor().equals("eval_loc")) {
        try {
        	result = model.eval_loc();
        } catch (Exception e) {
          e.printStackTrace();
        }
              
      }else if (action.equals(gb)) {
      result = model.getBeer();
      
    } else if (action.equals(hb)) {
      result = model.handInBeer();
      
    } else if (action.equals(sb)) {
      result = model.sipBeer();
      
    } else if (action.getFunctor().equals("deliver")) {
    // wait 4 seconds to finish "deliver"
      try { Thread.sleep(4000); } catch (Exception e) {}
      try {
		result = model.addBeer( (int)((NumberTerm)action.getTerm(1)).solve());
	} catch (NoValueException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
    } else {
      System.err.println("Failed to execute action "+action);
    }

    if (result) {
      updatePercepts();
      try { Thread.sleep(100); } catch (Exception e) {}
    }
    return result;
  }
}
