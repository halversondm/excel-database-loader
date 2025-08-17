import React from 'react';
import PropTypes from 'prop-types';
import { Home, Settings, Upload, User, X } from 'lucide-react';

const Sidebar = ({ isOpen, closeSidebar, setActiveView }) => {
    const menuItems = [
        { icon: Home, label: 'Dashboard', id: 'dashboard' },
        { icon: User, label: 'Profile', id: 'profile' },
        { icon: Settings, label: 'Settings', id: 'settings' },
        { icon: Upload, label: 'Upload', id: 'upload' },
    ];

    return (
        <>
            {/* Overlay for mobile */}
            {isOpen && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-50 z-20 lg:hidden"
                    onClick={closeSidebar}
                />
            )}

            {/* Sidebar */}
            <aside
                className={`
        fixed left-0 top-0 z-30 h-full w-64 bg-gray-900 text-white transform transition-transform duration-300 ease-in-out
        ${isOpen ? 'translate-x-0' : '-translate-x-full'}
        lg:translate-x-0 lg:static lg:z-auto
      `}
            >
                <div className="flex items-center justify-between p-4 border-b border-gray-700">
                    <h2 className="text-lg font-semibold">Navigation</h2>
                    <button
                        onClick={closeSidebar}
                        className="p-1 rounded-md hover:bg-gray-800 lg:hidden"
                    >
                        <X size={20} />
                    </button>
                </div>

                <nav className="mt-4">
                    {menuItems.map((item) => (
                        <button
                            key={item.id}
                            className="w-full flex items-center space-x-3 px-4 py-3 text-left hover:bg-gray-800 transition-colors"
                            onClick={() => setActiveView(item.id)}
                        >
                            <item.icon size={20} />
                            <span>{item.label}</span>
                        </button>
                    ))}
                </nav>
            </aside>
        </>
    );
};

Sidebar.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    closeSidebar: PropTypes.func.isRequired,
    setActiveView: PropTypes.func.isRequired,
};

export default Sidebar;
