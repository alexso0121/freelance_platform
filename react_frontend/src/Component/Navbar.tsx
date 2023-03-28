import React from 'react'
import { useState } from 'react'
//import {Bar3Icon,XMarkIcon } from '@heroicon/react'
const Navbar = () => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const flexBetween="flex items-center justify-between"
  return (

      <nav className="bg-gray-800">
        <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <a href="/" className="text-white font-bold text-xl">
                My Website
              </a>
            </div>
            <div className="hidden md:block">
              <div className="flex items-center">
                <a href="#" className="px-3 py-2 text-white">
                  Home
                </a>
                <a href="#" className="px-3 py-2 text-gray-400 hover:text-white">
                  About
                </a>
                <a href="#" className="px-3 py-2 text-gray-400 hover:text-white">
                  Contact
                </a>
              </div>
            </div>
            <div className="-mr-2 flex md:hidden">
              <button
                onClick={() => setIsMenuOpen(!isMenuOpen)}
                type="button"
                className="bg-gray-800 inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white"
                aria-controls="mobile-menu"
                aria-expanded="false"
              >
                <span className="sr-only">Open main menu</span>
                {/*
                  Icon when menu is closed.
    
                  Heroicon name: outline/menu
    
                  Menu open: "hidden", Menu closed: "block"
                */}
                <svg
                  className={`${isMenuOpen ? 'hidden' : 'block'} h-6 w-6`}
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                  aria-hidden="true"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4 6h16M4 12h16M4 18h16"
                  />
                </svg>
                {/*
                  Icon when menu is open.
    
                  Heroicon name: outline/x
    
                  Menu open: "block", Menu closed: "hidden"
                */}
                <svg
                  className={`${isMenuOpen ? 'block' : 'hidden'} h-6 w-6`}
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                  aria-hidden="true"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
        {/*
          Mobile menu, toggle classes based on menu state.
    
          Open: "block", closed: "hidden"
        */}
        <div className={`${isMenuOpen ? 'block' : 'hidden'} md:hidden`} id="mobile-menu">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
            <a href="#" className="block px-3 py-2 rounded-md text-base font"/>
            </div>
            </div>
            </nav>
    // <><nav>
    //       <div
    //           className={`${flexBetween}  top-0 z-30 w-full py-6 text-indigo-300 bg-slate-200 ` }>
    //             <div className={`${flexBetween} mx-auto w-5/6`}>
    //                 <div className={`${flexBetween} w-full gap-32`}>
    //                     {/* Left side */}
    //                         <div className={`${flexBetween} w-full`}> lefthfhjyhjghjghjug</div>
    //                         <div className={`${flexBetween} w-full gap-8`}>
    //                             <div className={`${flexBetween} gap-5 text-sm`}>
    //                                 {/* right */}
    //                                 <p>Home</p>
    //                                 <p>Pages</p>
    //                                 <p>Contact Us</p>
    //                             </div>
    //                             <div className={`${flexBetween} gap-8 `}>
    //                                 <p>signin</p>
    //                             </div>
                                
    //                             </div>
    //                 </div>
    //             </div>
    //       </div>
    //   </nav></>
  )
}

export default Navbar