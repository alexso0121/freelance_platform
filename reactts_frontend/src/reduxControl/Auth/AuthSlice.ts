import { createSlice } from "@reduxjs/toolkit"
import { PayloadAction } from "@reduxjs/toolkit/dist/createAction"

export interface Authi{
    islogin:boolean

}


 

const initalAuthState:Authi={
    islogin:false
}

export const AuthSlice=createSlice({
    name:"islogin",
    initialState:initalAuthState,
    reducers:{
        login(state){
            state.islogin=true
        }
        //,action:PayloadAction<{type:string}>
        ,logout:(state)=>{
            state.islogin=false
           
        }    }
})
export const {login,logout}=AuthSlice.actions
export default AuthSlice.reducer;
