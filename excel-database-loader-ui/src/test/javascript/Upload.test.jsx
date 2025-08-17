import {expect, vi} from 'vitest';
import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import Upload from '../../main/javascript/Upload';
import '@testing-library/jest-dom';

beforeEach(() => {
    global.fetch = vi.fn();
});

afterEach(() => {
    vi.resetAllMocks();
});

test('renders upload heading and drag/drop area', () => {
    render(<Upload/>);
    expect(screen.getByText(/Excel Upload/i)).toBeInTheDocument();
    expect(
        screen.getByText(/Drag and drop an Excel file here, or click to select/i)
    ).toBeInTheDocument();
});

test('shows "Uploading..." status and then response text on file upload', async () => {
    global.fetch.mockResolvedValueOnce({
        text: async () => 'Excel parsed successfully!',
    });

    render(<Upload/>);
    const fileInput = document.querySelector('input[type="file"]');

    // Simulate file selection
    const file = new File(['dummy content'], 'test.xlsx', {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });
    fireEvent.change(fileInput, {target: {files: [file]}});

    await waitFor(() => {
        expect(screen.getByText(/Excel parsed successfully!/i)).toBeInTheDocument();
    });
});

test('shows "Upload failed." on fetch error', async () => {
    global.fetch.mockRejectedValueOnce(new Error('Network error'));

    render(<Upload/>);
    const fileInput = document.querySelector('input[type="file"]');
    const file = new File(['dummy content'], 'test.xlsx', {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });
    fireEvent.change(fileInput, {target: {files: [file]}});

    await waitFor(() => {
        expect(screen.getByText(/Upload failed./i)).toBeInTheDocument();
    });
});

test('handles drag and drop file upload', async () => {
    global.fetch.mockResolvedValueOnce({
        text: async () => 'Excel parsed successfully!',
    });

    render(<Upload/>);
    const dropArea = screen
        .getByText(/Drag and drop an Excel file/i)
        .closest('div');
    const file = new File(['dummy content'], 'test.xlsx', {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });

    fireEvent.drop(dropArea, {
        dataTransfer: {
            files: [file],
        },
    });

    await waitFor(() => {
        expect(screen.getByText(/Excel parsed successfully!/i)).toBeInTheDocument();
    });
});
