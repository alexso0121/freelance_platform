import React from "react";
import { useState } from "react";

interface Props{
    name: string;
    age: number;
}
const NewComponent:React.FC<Props>=({name,age})=>{
    return (
        
        <div>
            <h1 className="text-3xl font-bold underline">dfsdfs</h1>
            <h1>{name}</h1>
            <h2>{age}</h2>
        </div>
        
    )
}

export default NewComponent;