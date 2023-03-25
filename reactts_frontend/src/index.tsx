import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter,Routes,Route} from 'react-router-dom';
import Hello from './Component/Hello';
import {Provider} from "react-redux"
import { store } from './reduxControl/store';
function Index(){

  return(
    <BrowserRouter>

      {/* <Layout  userStatus={userStatus}
  /> */}
      <Routes>


        <Route path="/" element={<App />} />
        <Route path='/test' element={<Hello />} />    
      </Routes>
  
    </BrowserRouter>
  );
} 
const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <Provider store={store}> 
         <Index />
         </Provider>

  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
