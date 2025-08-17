import React from 'react';
import PropTypes from 'prop-types';
import { Menu, User } from 'lucide-react';

// Header Component
const Header = ({ toggleSidebar, setActiveView }) => {
    return (
        <header className="bg-white shadow-sm border-b border-gray-200 px-4 py-3 flex items-center justify-between">
            <div className="flex items-center space-x-4">
                <button
                    onClick={toggleSidebar}
                    className="p-2 rounded-md hover:bg-gray-100 lg:hidden"
                >
                    <Menu size={20} />
                </button>
                <h1 className="text-xl font-semibold text-gray-900">
                    Homeowners
                </h1>
            </div>
            <div className="flex items-center space-x-4">
                <button
                    className="p-2 rounded-md hover:bg-gray-100"
                    onClick={() => setActiveView('profile')}
                >
                    <User size={20} />
                </button>
            </div>
        </header>
    );
};

Header.propTypes = {
    toggleSidebar: PropTypes.func.isRequired,
    setActiveView: PropTypes.func.isRequired,
};

export default Header;
