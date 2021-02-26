/* Initial beliefs */

// initially, I believe that there is some beer in the fridge
available(beer,fridge).

// my owner should not consume more than 10 beers a day :-)
limit(beer,10). 

/* Rules */ 

too_much(B) :- 
   .date(YY,MM,DD) &
   .count(consumed(YY,MM,DD,_,_,_,B),QtdB) &
   limit(B,Limit) &
   QtdB > Limit.

   
/* Plans */
//Objetivo de explorar general
!explore.

//Para obtener explorar muevete
@e1
+!explore : true <- !at(robot,location);!evaluate(location);-evaluate(location);!explore.

//Me dicen que debo evaluar la casilla a la que me he movido
@e2
+!evaluate(P) : at(robot,P) <- eval_loc(P);+evaluate(P);-at(robot,P).

//Moverme a un sitio y una vez ejecuto moverme se que estoy en ese sitio
@m1
+!at(robot,P) : not at(robot,P)
  <- move_towards(P); +at(robot,P).   

@e3
+at(robot,weakfire): true <- .print("WEAK FIRE").
/* 
@m1
+!at(robot,P) : at(robot,P) <- true.
@m2
+!at(robot,P) : not at(robot,P)
  <- move_towards(P);
     !at(robot,P).
*/
// when the supermarket makes a delivery, try the 'has' 
// goal again   
@a1
+delivered(beer,Qtd,OrderId)[source(supermarket)] : true
  <- +available(beer,fridge);
     !has(owner,beer). 

// when the fridge is opened, the beer stock is perceived
// and thus the available belief is updated
@a2
+stock(beer,0) 
   :  available(beer,fridge)
   <- -available(beer,fridge).

@a3
+stock(beer,N) 
   :  N > 0 & not available(beer,fridge)
   <- +available(beer,fridge).
