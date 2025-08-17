import React from 'react';

const Settings = () => {
    return (
        <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-6">Settings</h2>
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 max-w-2xl">
                <div className="space-y-6">
                    <div>
                        <h3 className="text-lg font-medium text-gray-900 mb-3">
                            Preferences
                        </h3>
                        <div className="space-y-3">
                            <label className="flex items-center">
                                <input type="checkbox" className="mr-3" />
                                <span className="text-gray-700">
                                    Enable notifications
                                </span>
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
};

export default Settings;
