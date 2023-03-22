import React from "react";
import NewComponent from "./NewComponent";





function Hello(){
    const name:string="alex";
    const num:number=7+5;
    
     return (
       <div>
        <NewComponent age={num} name={name} />
        
       </div>
     )
}

export default Hello;