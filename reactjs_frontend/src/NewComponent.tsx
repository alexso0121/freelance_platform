import React from "react";


interface Props{
    name: string;
    age: number;
}
const NewComponent:React.FC<Props>=({name,age})=>{
    return (
        <div>
            <h1>dfsdfs</h1>
            <h1>{name}</h1>
            <h2>{age}</h2>
        </div>
       
    )
}

export default NewComponent;