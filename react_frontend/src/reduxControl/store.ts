import { configureStore } from '@reduxjs/toolkit'
import AuthReducer from './Auth/AuthSlice'
import storage from 'redux-persist/lib/storage';
import { persistReducer, persistStore } from 'redux-persist';
import thunk from 'redux-thunk';

const persistConfig={
  key:'root',
  storage,
}

const persistedReducer=persistReducer(persistConfig,AuthReducer)

export const store = configureStore({
  reducer: persistedReducer,
  middleware:[thunk]
  // {
  //   islogin: AuthReducer,
  
  // },
})

export const persistor = persistStore(store)
export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch