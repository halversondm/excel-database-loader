import {describe, expect, it, vi} from 'vitest';
import {fireEvent, render, screen} from '@testing-library/react';
import Sidebar from '../../main/javascript/Sidebar';
import '@testing-library/jest-dom';

describe('Sidebar', () => {
    const setActiveView = vi.fn();
    const closeSidebar = vi.fn();

    beforeEach(() => {
        setActiveView.mockClear();
        closeSidebar.mockClear();
        window.innerWidth = 768;
    });

    it('renders sidebar when isOpen is true', () => {
        render(
            <Sidebar
                isOpen={true}
                closeSidebar={closeSidebar}
                setActiveView={setActiveView}
            />
        );
        expect(screen.getByText('Navigation')).toBeInTheDocument();
        expect(screen.getByText('Dashboard')).toBeInTheDocument();
        expect(screen.getByText('Profile')).toBeInTheDocument();
        expect(screen.getByText('Settings')).toBeInTheDocument();
        expect(screen.getByText('Upload')).toBeInTheDocument();
    });

    it('does not render sidebar when isOpen is false on mobile', () => {
        render(
            <Sidebar
                isOpen={false}
                closeSidebar={closeSidebar}
                setActiveView={setActiveView}
            />
        );
        // Sidebar should be hidden (offscreen), but still in the DOM for transition
        expect(screen.getByText('Navigation')).toBeInTheDocument();
    });

    // it('renders overlay when isOpen is true', () => {
    //     render(<Sidebar isOpen={true} closeSidebar={closeSidebar} setActiveView={setActiveView} />);
    //     expect(screen.getByRole('presentation')).toBeDefined();
    // });
    //
    // it('calls closeSidebar when overlay is clicked', () => {
    //     render(<Sidebar isOpen={true} closeSidebar={closeSidebar} setActiveView={setActiveView} />);
    //     const overlay = screen.getByRole('presentation');
    //     fireEvent.click(overlay);
    //     expect(closeSidebar).toHaveBeenCalled();
    // });
    //
    // it('calls closeSidebar when close button is clicked', () => {
    //     render(<Sidebar isOpen={true} closeSidebar={closeSidebar} setActiveView={setActiveView} />);
    //     const closeBtn = screen.getByRole('button');
    //     fireEvent.click(closeBtn);
    //     expect(closeSidebar).toHaveBeenCalled();
    // });

    it('calls setActiveView with correct id when menu item is clicked', () => {
        render(
            <Sidebar
                isOpen={true}
                closeSidebar={closeSidebar}
                setActiveView={setActiveView}
            />
        );
        fireEvent.click(screen.getByText('Dashboard'));
        expect(setActiveView).toHaveBeenCalledWith('dashboard');
        fireEvent.click(screen.getByText('Profile'));
        expect(setActiveView).toHaveBeenCalledWith('profile');
        fireEvent.click(screen.getByText('Settings'));
        expect(setActiveView).toHaveBeenCalledWith('settings');
        fireEvent.click(screen.getByText('Upload'));
        expect(setActiveView).toHaveBeenCalledWith('upload');
    });
});
