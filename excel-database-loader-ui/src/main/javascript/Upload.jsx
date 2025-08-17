import React, { useRef, useState } from 'react';

const Upload = () => {
    const [uploadStatus, setUploadStatus] = useState('');
    const fileInputRef = useRef(null);

    const handleDrop = async (e) => {
        e.preventDefault();
        const file = e.dataTransfer.files[0];
        if (file) {
            await uploadFile(file);
        }
    };

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (file) {
            await uploadFile(file);
        }
    };

    const uploadFile = async (file) => {
        setUploadStatus('Uploading...');
        const formData = new FormData();
        formData.append('file', file);

        try {
            const res = await fetch('/api/v1/excel/parse', {
                method: 'POST',
                body: formData,
            });
            const text = await res.text();
            setUploadStatus(text);
        } catch {
            setUploadStatus('Upload failed.');
        }
    };

    return (
        <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-6">
                Excel Upload
            </h2>
            <div
                className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 max-w-2xl flex flex-col items-center"
                onDrop={handleDrop}
                onDragOver={(e) => e.preventDefault()}
                style={{ cursor: 'pointer' }}
                onClick={() => fileInputRef.current.click()}
            >
                <div className="space-y-4 w-full text-center">
                    <div className="py-10 border-2 border-dashed border-blue-400 rounded-md">
                        <p className="text-gray-700">
                            Drag and drop an Excel file here, or click to select
                        </p>
                        <input
                            id="fileInput"
                            type="file"
                            accept=".xlsx,.xls"
                            ref={fileInputRef}
                            style={{ display: 'none' }}
                            onChange={handleFileChange}
                        />
                    </div>
                    {uploadStatus && (
                        <div className="mt-4 text-sm text-gray-600">
                            {uploadStatus}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Upload;
