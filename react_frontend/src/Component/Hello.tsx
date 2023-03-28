import React, { useEffect, useState } from "react";
import NewComponent from "./NewComponent";

import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../reduxControl/store";
import { login,logout } from "../reduxControl/Auth/AuthSlice";





function Hello(){
    const name:string="alex";
    const num:number=7+5;
    const AuthStatus = useSelector((state: RootState) => state.islogin);
    const dispatch = useDispatch();
    
    const [fee,setFee]=useState<number | null>(null)
    const handleClick=()=>{
       
        setFee(4)
        dispatch(login())
        
    }
    const handlelogout=()=>{
        dispatch(logout)
    }
    
    useEffect(()=>{
        console.log(fee)
        console.log(AuthStatus)
    },[fee,AuthStatus])

     return (
       <div className="text-3xl font-bold underline">
        <NewComponent age={num} name={name} />
        <button onClick={handleClick} type="button">Clickme</button>
        {fee!==null?<p>{fee}</p>:null}
        
       </div>
        
     )
}

export default Hello;