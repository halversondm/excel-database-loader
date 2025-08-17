import React, { useEffect, useState } from 'react';
import MainContent from './MainContent';
import Sidebar from './Sidebar';
import Header from './Header';

// Main App Component
const App = () => {
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [activeView, setActiveView] = useState('dashboard');

    const toggleSidebar = () => {
        setSidebarOpen(!sidebarOpen);
    };

    const closeSidebar = () => {
        setSidebarOpen(false);
    };

    // Close sidebar on window resize to large screens
    useEffect(() => {
        const handleResize = () => {
            if (window.innerWidth >= 1024) {
                setSidebarOpen(false);
            }
        };

        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);

    return (
        <div className="h-screen flex flex-col bg-gray-100">
            <Header
                toggleSidebar={toggleSidebar}
                setActiveView={setActiveView}
            />

            <div className="flex flex-1 overflow-hidden">
                <Sidebar
                    isOpen={sidebarOpen}
                    closeSidebar={closeSidebar}
                    setActiveView={setActiveView}
                />

                <div className="flex-1 flex flex-col lg:ml-0">
                    <MainContent activeView={activeView} />
                </div>
            </div>
        </div>
    );
};

export default App;
