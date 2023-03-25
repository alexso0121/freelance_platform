import { configureStore } from '@reduxjs/toolkit'
import AuthReducer from './Auth/AuthSlice'

export const store = configureStore({
  reducer: {
    islogin: AuthReducer,
  },
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch