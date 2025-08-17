import React from 'react';
import { expect, vi, beforeEach, afterEach, test } from 'vitest';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import Dashboard from '../../main/javascript/Dashboard';

beforeEach(() => {
    // Reset fetch mock before each test
    vi.stubGlobal('fetch', vi.fn());
});

afterEach(() => {
    vi.resetAllMocks();
});

test('renders Dashboard and search input', () => {
    render(<Dashboard />);
    expect(screen.getByText(/Dashboard/i)).toBeInTheDocument();
    expect(
        screen.getByPlaceholderText(/Enter your search term/i)
    ).toBeInTheDocument();
});

test('does not fetch when searchTerm is empty', async () => {
    render(<Dashboard />);
    expect(fetch).not.toHaveBeenCalled();
});

test('fetches and displays results on search', async () => {
    const mockResults = [
        {
            address: '123 Main St',
            lastName: 'Smith',
            firstName: 'John',
            secondName: 'A.',
        },
        {
            address: '456 Oak Ave',
            lastName: 'Doe',
            firstName: 'Jane',
            secondName: 'B.',
        },
    ];
    fetch.mockResolvedValueOnce({
        json: async () => mockResults,
    });

    render(<Dashboard />);
    const input = screen.getByPlaceholderText(/Enter your search term/i);
    fireEvent.change(input, { target: { value: 'Smith' } });

    await waitFor(() => {
        expect(fetch).toHaveBeenCalledWith(
            '/api/v1/excel/retrieve?search=Smith'
        );
        expect(screen.getByText('123 Main St')).toBeInTheDocument();
        expect(screen.getByText('456 Oak Ave')).toBeInTheDocument();
        expect(screen.getByText(/Last Name: Smith/)).toBeInTheDocument();
        expect(screen.getByText(/Last Name: Doe/)).toBeInTheDocument();
    });
});

test('shows no results if fetch fails', async () => {
    fetch.mockRejectedValueOnce(new Error('API error'));
    render(<Dashboard />);
    const input = screen.getByPlaceholderText(/Enter your search term/i);
    fireEvent.change(input, { target: { value: 'Error' } });

    await waitFor(() => {
        expect(fetch).toHaveBeenCalled();
        // No result cards should be rendered
        expect(screen.queryByText(/Last Name:/)).not.toBeInTheDocument();
    });
});
