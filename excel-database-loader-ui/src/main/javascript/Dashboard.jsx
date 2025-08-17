import {useEffect, useState} from 'react';

const Dashboard = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [results, setResults] = useState([]);

    useEffect(() => {
        if (searchTerm.trim() === '') {
            setResults([]);
            return;
        }
        fetch(`/api/v1/excel/retrieve?search=${encodeURIComponent(searchTerm)}`)
            .then((res) => res.json())
            .then((data) => setResults(data))
            .catch(() => setResults([]));
    }, [searchTerm]);

    return (
        <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-6">Dashboard</h2>
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 max-w-2xl">
                <div className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Search
                        </label>
                        <input
                            type="text"
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="Enter your search term"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                </div>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {results.map((item, idx) => (
                    <div
                        key={idx}
                        className="bg-white p-6 rounded-lg shadow-sm border border-gray-200"
                    >
                        <h3 className="text-lg font-semibold text-gray-900 mb-2">
                            {item.address}
                        </h3>
                        <p className="text-gray-600">Last Name: {item.lastName}</p>
                        <p className="text-gray-600">First Name: {item.firstName}</p>
                        <p className="text-gray-600">Second Name: {item.secondName}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Dashboard;
