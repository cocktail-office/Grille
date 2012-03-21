/**
 Validation des champs du formulaire, si l'element est un textarea,
 si il a un attribut maxLength, la longueur max sera maxlength sinon
 et de toute facons, la longeur max est de 4000 char.
 */
function validTextForm (leForm) {    
    var zones = leForm.elements;
    var maxLength = 3999 ;
    for (var i = 0; i < zones.length; i++ ) {
        if ( zones[i].type == "textarea"  ) {
            var maxZone = zones[i].getAttribute('maxLength');
            var max = ((maxZone != null)?maxZone:maxLength);                  
            //on vire les \r present si on est sous IE
            var str = zones[i].value.replace(/\r/gi,'');
            //comptage des retours chariot : js 1 retour chariot = 2 caracteres Oracle                        
            var nbRet = str.match(/\n/gi);
            var zoneLength = str.length;           
            if (nbRet) zoneLength += nbRet.length;              
            if ( zoneLength > max ) {
                alert(" La zone " + zones[i].id + " est trop longue  !! \n"
                    + " Longueur actuelle : "+zoneLength+"\n"
                    + " Maximum possible : "+(max));
                zones[i].focus();
                return false;
            }
        }
    }    
   return true;
}

/*ouvre une fenetre sur le curseur ou centree sur l'ecran*/
function ouvreFenetre(larg, haut,fenetre,e,centre,resizable,scroll){
	var posx = 0;
	var posy = 0;
	
	if (centre=='O'){
		var y=(screen.height-haut)/2;
  		var x=(screen.width-larg)/2;
	
	}else{		
		
		//CALCUL position curseur
		var x = (navigator.appName.substring(0,3) == "Net") ? e.pageX+document.body.scrollLeft+window.screenX : event.x+window.screenLeft;
		var y = (navigator.appName.substring(0,3) == "Net") ? e.pageY-document.body.scrollTop+window.screenY  : event.y+window.screenTop;		
  		
		y+=100;
		x-=(larg/2);
	}
	// positionnement des options de la fenetre
	var options = "height="+ haut +",width="+ larg+",top="+y+",left="+x;
	options +=",status=no, location=no, toolbar=no,directories=no";
	if (resizable=='O'){
		options+=",resizable=yes";
	}else{
		options+=",resizable=no";
	}
	if (scroll=='N'){
		options+=",scrollbars=no";
	}else{
		options+=",scrollbars=yes";
	}
	
	//creation de la fenetre   	
	var popup = window.open ("" , fenetre, options);	
	if (!popup.opener)
		popup.opener = self;
	
	//positionnement du focus
	if (window.focus)
		popup.focus();
}

/* ouvre une fenetre avec window.js*/
function openWindow( width,height,title,statusInfo,idContent,e){
	if (win != undefined) {
	
	}else{ 
		win = new Window({className: "dialog", width:width, height:height, zIndex: 100, resizable: true, title: title, showEffect:Effect.BlindDown, hideEffect: Effect.SwitchOff, draggable:true, wiredDrag: true});
	}
	win.setContent(idContent, true, true) ;
	win.setStatusBar(statusInfo);	
	win.showCenter();
}

/*  ferme le popup et fais un refresh sur le parent en positionnant une ancre */
function refreshParentAndClose(ancre,close) {		
	window.opener.document.getElementById(ancre).click();
	if (close=='O'){
		window.close();
		window.opener.focus();
	}
}

/* Affiche ou cache une div */
function showMenu(menu){
	if (menu.visible()) {
		//menu.hide();
		Effect.BlindUp(menu, {duration:0.5});
	}else{
		//menu.show();
		Effect.BlindDown(menu, {duration:0.5});
	}
}

/* applique le style CSS selected a l'element */
var oldSelectedRow;

var oldClassRow;
function selectThis(elem){
	if (window.oldClassRow != undefined) {
		oldSelectedRow.className = oldClassRow;		
	}
	alert(elem.className);
	oldClassRow = elem.className;	
	elem.className = "selected";
	alert(elem.className);
	oldSelectedRow = elem;
	return true;	
}

/* ouvre un popup avec la lib js window.js
title : titre de la fenetre
statusInfo : infos dans la barre de status
idContent : id de la div de contenue
w : width
h : height
 */
var win;
Window.hasEffectLib = false;
function openWindowt(title,statusInfo,idContent,w,h,topWin,leftWin,center){
      	var closable=true;
      	var modalWin = false;
      	var resizableWin = false;
      	//l'argument closable est le 9eme passé      	
      	if (arguments.length>=9){
      		closable=arguments[9];
      	}
      	//l'argument modal est le 10eme passé      	
      	if (arguments.length>=10){
      		modalWin=arguments[10];
      	}      
      	//l'argument resizable est le 11eme passé      	
      	if (arguments.length>=11){
      		resizableWin=arguments[11];
      	}
      		
      	if (win != undefined) {
      		win.centered = false;
      		win.setSize(w,h);
      		win.show(modalWin);
      	}else{       			
      		win = new Window({className: "dialog",width:w,height:h,zIndex: 100, resizable: resizableWin, title: title, draggable:true, wiredDrag: true,closable:closable,showEffectOptions: {duration: null}});
      		win.setStatusBar(statusInfo);
      		win.setLocation(topWin, leftWin);      		
      		if (center=='O') {
      			win.showCenter(modalWin);
      		} else {
      			win.show(modalWin);
      		}		
      	}
      	
      	win.setContent(idContent,false,false) ;	      	       	
}
	

function testMailValid(mail){
	var re=RegExp("^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+((\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)?)+@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9\-]*[a-zA-Z0-9])?$");
	if (!re.test(mail.value)){
		alert('L\'adresse mail '+mail.value+' est invalide !!');
		setTimeout(function() { mail.focus();mail.select(); }, 100);
		return false;
	}	
	return true;	
} 

function testNote(field){
	var re=RegExp("^[0-9]+([\.,][0-9]+)?$");
	var retour = false;
	if (!re.test(field.value)){
		alert('La note '+field.value+' n\'est pas un nombre décimal !!');
		setTimeout(function() { field.focus(); field.select();}, 100);
		return false;
	}
	if (field.value>99) {
		alert('La note '+field.value+' est limitée à 2 chiffres !!');
		setTimeout(function() { field.focus(); field.select();}, 100);
		return false;
	}	
	return true;
}