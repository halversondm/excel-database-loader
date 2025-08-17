import React from 'react';
import { describe, expect, it, vi } from 'vitest';
import { fireEvent, render, screen } from '@testing-library/react';
import Header from '../../main/javascript/Header';
import '@testing-library/jest-dom';

describe('Header', () => {
    it('renders title and buttons', () => {
        render(<Header toggleSidebar={() => {}} setActiveView={() => {}} />);
        expect(screen.getByText('Homeowners')).toBeInTheDocument();
    });

    it('calls toggleSidebar when menu button is clicked', () => {
        const toggleSidebar = vi.fn();
        render(
            <Header toggleSidebar={toggleSidebar} setActiveView={() => {}} />
        );
        const menuButton = screen.getAllByRole('button')[0];
        fireEvent.click(menuButton);
        expect(toggleSidebar).toHaveBeenCalled();
    });

    it('calls setActiveView with "profile" when user button is clicked', () => {
        const setActiveView = vi.fn();
        render(
            <Header toggleSidebar={() => {}} setActiveView={setActiveView} />
        );
        const userButton = screen.getAllByRole('button')[1];
        fireEvent.click(userButton);
        expect(setActiveView).toHaveBeenCalledWith('profile');
    });
});
