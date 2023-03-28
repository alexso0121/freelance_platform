import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import Navbar from './Component/Navbar';
import {BrowserRouter,Routes,Route} from 'react-router-dom';
import Hello from './Component/Hello';
import {Provider} from "react-redux"
import { store,persistor } from './reduxControl/store';
import { PersistGate } from 'redux-persist/integration/react';
function Index(){

  return(
    <BrowserRouter>
      <Navbar />
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
      <PersistGate loading={null} persistor={persistor} >
        <Index />
      </PersistGate>
         
         </Provider>

  </React.StrictMode>
);


