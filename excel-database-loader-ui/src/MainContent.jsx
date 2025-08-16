// Main Content Component
const MainContent = ({ activeView }) => {
    const renderContent = () => {
        switch(activeView) {
            case 'dashboard':
                return (
                    <div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Dashboard</h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                                <h3 className="text-lg font-semibold text-gray-900 mb-2">Card 1</h3>
                                <p className="text-gray-600">This is a sample card with some content.</p>
                            </div>
                            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                                <h3 className="text-lg font-semibold text-gray-900 mb-2">Card 2</h3>
                                <p className="text-gray-600">Another card with different content.</p>
                            </div>
                            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                                <h3 className="text-lg font-semibold text-gray-900 mb-2">Card 3</h3>
                                <p className="text-gray-600">A third card to complete the layout.</p>
                            </div>
                        </div>
                    </div>
                );
            case 'profile':
                return (
                    <div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Profile</h2>
                        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 max-w-2xl">
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
                                    <input
                                        type="text"
                                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                        placeholder="Enter your name"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                                    <input
                                        type="email"
                                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                        placeholder="Enter your email"
                                    />
                                </div>
                                <button className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition-colors">
                                    Save Changes
                                </button>
                            </div>
                        </div>
                    </div>
                );
            case 'settings':
                return (
                    <div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Settings</h2>
                        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 max-w-2xl">
                            <div className="space-y-6">
                                <div>
                                    <h3 className="text-lg font-medium text-gray-900 mb-3">Preferences</h3>
                                    <div className="space-y-3">
                                        <label className="flex items-center">
                                            <input type="checkbox" className="mr-3" />
                                            <span className="text-gray-700">Enable notifications</span>
                                        </label>
                                        <label className="flex items-center">
                                            <input type="checkbox" className="mr-3" />
                                            <span className="text-gray-700">Dark mode</span>
                                        </label>
                                        <label className="flex items-center">
                                            <input type="checkbox" className="mr-3" />
                                            <span className="text-gray-700">Auto-save</span>
                                        </label>
                                    </div>
                                </div>
                                <button className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition-colors">
                                    Update Settings
                                </button>
                            </div>
                        </div>
                    </div>
                );
            default:
                return (
                    <div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Welcome</h2>
                        <p className="text-gray-600">Select an option from the sidebar to get started.</p>
                    </div>
                );
        }
    };

    return (
        <main className="flex-1 p-6 bg-gray-50 overflow-auto">
            {renderContent()}
        </main>
    );
};

export default MainContent;