// Main Content Component
import Dashboard from './Dashboard';
import Profile from './Profile';
import Settings from './Settings';
import Upload from './Upload';

const MainContent = ({ activeView }) => {
    const renderContent = () => {
        switch (activeView) {
            case 'dashboard':
                return <Dashboard/>;
            case 'profile':
                return <Profile/>;
            case 'settings':
                return <Settings/>;
            case 'upload':
                return <Upload/>;
            default:
                return (
                    <div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Welcome</h2>
                        <p className="text-gray-600">
                            Select an option from the sidebar to get started.
                        </p>
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
