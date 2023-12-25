


onload = function () {
  //this is necessary, because DOM totatlly loads on load
  /*       document.getElementsByClassName("text-choice")[0].onmouseover =
                                                                                                                                                                                                      new Function("info()"); */
  ClassId = "text-choice";
  objects = document.getElementsByClassName(ClassId);
  length = objects.length;
  mouseOverPop(ClassId, length);


}; // end of function on load

const copyContent = async () => {
  text = document.documentElement.outerHTML;
  try {
    await navigator.clipboard.writeText(text);
    console.log("Content copied to clipboard");
  } catch (err) {
    console.error("Failed to copy: ", err);
  }



  nC = nS = 10;
  nE = 5;
  C = [];
  E = [];
  S = [];
  ES = [];

  for (i = 0; i < nC; i++)
    C[i + 1] = {
      id: i + 1,
      lib: "C" + Number(i + 1),
    };
  for (i = 0; i < nE; i++) {
    id = i + 1;
    lib = "E" + id;
    E[id] = {
      id: id,
      lib: lib,
    }; // end of Object E affectation
    クラス = "parent "; //cu10s
    i == 0 // below if true
      ? // if true here
      ((クラス += "active"), (atr = "disabled")) //below if false
      : // here is false within multiple ternary

      ((クラス += "idle1"), (atr = ""));
    header = document.querySelector(".header");
    header.innerHTML +=
      "<button id=" +
      id +
      ' class="' +
      クラス +
      '" onclick="buttonClick()"' +
      atr +
      "  >" +
      lib +
      "</button>";


    // check the code below for S, S cannot function here due to the fact that the rest of the DOM that we strive to use isn't found here

    console.log(
      "object : " + document.getElementsByClassName("text-choice")[0]
    ); // this would return undefined cause the object is yet to load; that's why we're going to have to use the onload function
    // in anguar it should not return undefined

    //db load on top




    //below background change


    bg_url = "https://www.wired.com/wp-content/uploads/2014/11/1wKJozK80xjv3dD609wE-aQ-1.gif";
    no_bg = "#";

    Background_img_on = true;



    for (i = 0; i < nS; i++) {
      id = i + 1;
      lib = "S" + id;

      S[id] = {
        id: id,
        lib: lib,
      }; // end of S object Affectation

      child = document.querySelector(".child");
      child.innerHTML +=
        '<button class= "text-choice idle" onclick="buttonClick()" id=' +
        id +
        ">" +
        lib +
        " </button> ";

      //
    } // end of for S
    ES_Counter = 0;



  } // end of for E
  //console.log(ClassId +"  &  "  + length);
}; // end of onload function

function mouseOverPop(CID, N) {
  objects = document.getElementsByClassName(CID);
  for (i = 0; i < N; i++) {
    object = objects[i];
    let k = i + 1;
    popClassSelector = ".popover";
    pop = document.querySelector(popClassSelector);

    object.onmouseover = function () {
      popTrigger(k, objects, pop);
    };

    object.onmouseout = function () {
      popTrigger(k, objects, pop);
    };

    // end of object mouseover
    //console.log("this is object number " + k);

    /*         console.log("& the method is: ");
                                                                                                                                                                                                                                                                                                      console.log(object.mouseover);  */

    /*  // if object is selected popTrigger shall trigger a popover
                                                                                                                                                                                                                                                                                                      popTrigger(object, ".popover"); */
  } //end of for

  /* console.log("For elements with the class : \""+CID+"\" we have "+N+" objects; and the full collection is :");

console.log(objects); */
} // end of function mouseOverPop

function popTrigger(i, objects, pop) {
  //
  //console.log("popover on " + i);

  //console.log(pop);

  behavior = event.type;
  s = "";

  "mouseout" == behavior ? popKiller(pop) : popStar(i, objects, pop);

  console.log(behavior); //"this event is about : "

  display = pop.style.display;

  display == "block" ? "pop" : "unable to pop";
  // to here
  console.log(display);
} //end of function popTrigger

function popStar(k, objects, pop) {
  console.log("I'm a popstar");

  selected = objects[k - 1].classList.contains("selected");
  console.log("is selected : " + selected);
  // from here
  selected ? popover(pop, objects[k - 1]) : popKiller(pop);

  // this indicates a behavior related to on mouse over
} //end of popstar Function

function popover(pop, object) {
  // display popover
  pop.style.left = object.offsetLeft + object.offsetWidth + 10 + "px";
  pop.style.top = object.offsetTop + "px";
  pop.style.display = "block";
  //console.log(true);
  //none
} //end of function pop

function popKiller(pop) {
  pop.style.display = "";
}

function ES_insertion(Sid) {
  ES_Counter++;

  ES[ES_Counter] = {
    id: ES_Counter,
    id_E: document.querySelector(".active").id,
    id_S: Number(Sid),
    X_2: 0,
    start: null,
    end: null,
    date: null,
    status: -1,
  };

  console.log("This is an insertion of ES [ " + ES_Counter + " ] = ");
  console.log(ES[ES_Counter]);
  console.log(" ES = ");
  console.log(ES);
  //alert(ES[ES_Counter]);
} // end of ES_insertion Function

function ES_delete(Sid) {
  NewES = [];
  counter = 0;

  for (var key in ES) {
    id = ES[key].id_S;
    papaid = ES[key].id_E;
    pid = document.querySelector(".active").id;
    can_delete = id == Sid && papaid == pid;

    if (can_delete) {
      console.log(
        "DELETE FROM  ES WHERE  id_E = " + pid + " AND id_S = " + Sid
      );
      console.log(ES[key]);
      console.log("↑ ROW above has been deleted ↑");

      console.log(" ES = ");

      delete ES[key];
      //console.log(ES);
      //break;
    } // end of if can-delete then ( do on condition )
    else {
      counter++;
      NewES[counter] = ES[key];
      ES_Counter = counter;
    } //end of if can_delete

    /*   can_delete ?
                                                                                                                                                                                                                                                                (
                                                                                                                                                                                                                                                                    console.log("deleted :"),
                                                                                                                                                                                                                                                                    console.log(ES[key]),

                                                                                                                                                                                                                                                                    console.log(" ES = "),

                                                                                                                                                                                                                                                                    delete ES[key],
                                                                                                                                                                                                                                                                    console.log(ES)





                                                                                                                                                                                                                                                                    //@REM  end of ternary sid,
                                                                                                                                                                                                                                                                    //@REM  if sid is true
                                                                                                                                                                                                                                                                ) :
                                                                                                                                                                                                                                                                (
                                                                                                                                                                                                                                                                    1

                                                                                                                                                                                                                                                                    /*                         console.log("id = " + id + " ; Sid = " + Sid),
                                                                                                                                                                                                                                                                                            console.log("id mismatch, unable to delete"), */
    //continue doesn't work in ternary

    /*  ) */

    // end of ternary if !sid
  } // end of for key in ES
  ES = NewES;
  console.log(ES);
} // end of _delete function

function childplay(e) {
  /* ES_Counter++; */
  Ｉｄ = e.id;
  e.classList.contains("selected") ?
    ES_delete(Ｉｄ) :
    /* c = "delete" */
    ES_insertion(Ｉｄ) /* c = "add", here happens the insertion */;
  //alert(c);
  e.classList.toggle("selected");
  e.classList.toggle("idle");
} //end of childplay(e)

function buttonClick() {
  //var button = document.querySelector(".parent");
  button = event.target;
  button.classList.add("clicked");
  copyContent();

  setTimeout(() => {
    //console.log("Delayed for 0.5 seconds");
    button.classList.remove("clicked");
  }, "500"); // because animation of click lasts 0.5 seconds

  PARENT = button.classList.contains("parent");

  !PARENT
    ? // below this line lies a child node
    (childplay(button),
      consoleimg.load(
        "  https://defaced.dev/tools/consoleimg/rick.gif"
        /*  {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         size: 320,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         color: "#FFFFFF",
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       } */
      )) // multiple ternary operations we do A? (B,C) : F; could be (F,D)
    : // if it's a parent then  the expected behavior should happen below

    //below is what happens when a new parent is chosen
    ((c = confirm(
      "do you really want to switch Parent into " + button.innerText
    )),
      c ?
        ((active_parent = document.querySelector(".active")),
          (active_parent.disabled = 0),
          active_parent.classList.remove("active"),
          active_parent.classList.add("idle1"),
          button.classList.toggle("active"),
          button.classList.toggle("idle1"),
          (button.disabled = 1),
          console.log(
            "%cNo one ever talks about the updog ❓😭🐶🐕🐩🐕‍🦺🦮😟😰❓",
            "font-size:56px; text-shadow: 3px 3px 0 rgb(217,31,38) , 6px 6px 0 rgb(226,91,14) , 9px 9px 0 rgb(245,221,8) , 12px 12px 0 rgb(5,148,68) , 15px 15px 0 rgb(2,135,206) , 18px 18px 0 rgb(4,77,145) , 21px 21px 0 rgb(42,21,113);"
          )) // end of ternary if Confirm == true
        :
        console.log("operation Cancelled"));
  //end of ternary if Confirm== false //end of ternary C ?( if true ): (else do) // end ternary is not a parent ;

  /*       button.classList.toggle("selected");
                                                                                                                                                                                                    button.classList.toggle("idle"); */

  //pdisplay = popover.style.display;

  // Position the popover relative to the selected button
  /*       if (button.classList.contains("selected")) {
                                                                                                                                                                                                      popover.style.left = button.offsetLeft + button.offsetWidth + 10 + "px";
                                                                                                                                                                                                      popover.style.top = button.offsetTop + "px";
                                                                                                                                                                                                      popover.style.display = pdisplay == "block" ? "none" : "block";
                                                                                                                                                                                                      //none
                                                                                                                                                                                                    } else {
                                                                                                                                                                                                      // text choice isn't selected
                                                                                                                                                                                                      popover.style.display = "none";
                                                                                                                                                                                                    } // end of btn if contains selected */
} // end of btnClick function

function I_hate_the_background_image() {
  if (Background_img_on) {
    document.body.style.backgroundImage = 'url(' + no_bg + ')';

    // turns off bg_of_body
    Background_img_on = false;
  } //end of if bg_img_is_on
}

function I_love_the_animated_bg_image() {

  if (!Background_img_on) {
    document.body.style.backgroundImage = "url(" + bg_url + ')';

    Background_img_on = true;
  }

} // end of background images are the love of my life
