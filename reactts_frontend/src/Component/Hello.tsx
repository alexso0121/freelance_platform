import React, { useEffect, useState } from "react";
import NewComponent from "./NewComponent";
import '../Styles/Hello.css';






function Hello(){
    const name:string="alex";
    const num:number=7+5;
    const [fee,setFee]=useState<number | null>(null)
    const handleClick=()=>{
        
        setFee(4)
    
        
    }
    useEffect(()=>{
        console.log(fee)
    },[fee])
     return (
       <div className="Test">
        <NewComponent age={num} name={name} />
        <button onClick={handleClick} type="button">Clickme</button>
        {fee!==null?<p>{fee}</p>:null}
       </div>
        
     )
}

export default Hello;