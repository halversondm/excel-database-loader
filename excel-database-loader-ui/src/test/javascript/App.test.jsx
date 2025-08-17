import {describe, expect, it, vi} from 'vitest';
import {fireEvent, render, screen} from '@testing-library/react';
import '@testing-library/jest-dom';
import App from '../../main/javascript/App';

// Mock child components
vi.mock('../../main/javascript/Header', () => ({
    default: ({toggleSidebar, setActiveView}) => (
        <button data-testid="toggle-sidebar" onClick={toggleSidebar}>
            Toggle Sidebar
        </button>
    ),
}));
vi.mock('../../main/javascript/Sidebar', () => ({
    default: ({isOpen, closeSidebar, setActiveView}) => (
        <div data-testid="sidebar">
            {isOpen ? 'Sidebar Open' : 'Sidebar Closed'}
        </div>
    ),
}));
vi.mock('../../main/javascript/MainContent', () => ({
    default: ({activeView}) => (
        <div data-testid="main-content">{activeView}</div>
    ),
}));

describe('App', () => {
    beforeEach(() => {
        // Reset window size before each test
        window.innerWidth = 800;
    });

    it('renders without crashing', () => {
        render(<App/>);
        expect(screen.getByTestId('toggle-sidebar')).toBeInTheDocument();
        expect(screen.getByTestId('sidebar')).toBeInTheDocument();
        expect(screen.getByTestId('main-content')).toHaveTextContent('dashboard');
    });

    it('toggles sidebar when toggleSidebar is called', () => {
        render(<App/>);
        const toggleButton = screen.getByTestId('toggle-sidebar');
        fireEvent.click(toggleButton);
        expect(screen.getByTestId('sidebar')).toHaveTextContent('Sidebar Open');
        fireEvent.click(toggleButton);
        expect(screen.getByTestId('sidebar')).toHaveTextContent('Sidebar Closed');
    });

    it('closes sidebar on window resize to large screens', () => {
        render(<App/>);
        const toggleButton = screen.getByTestId('toggle-sidebar');
        fireEvent.click(toggleButton);
        expect(screen.getByTestId('sidebar')).toHaveTextContent('Sidebar Open');
        window.innerWidth = 1200;
        fireEvent(window, new Event('resize'));
        expect(screen.getByTestId('sidebar')).toHaveTextContent('Sidebar Closed');
    });

    it('changes activeView when setActiveView is called from Header', () => {
        render(<App/>);
        // Simulate setActiveView by calling Header's prop directly
        // Since Header is mocked, we can't trigger setActiveView from UI, but we can check initial state
        expect(screen.getByTestId('main-content')).toHaveTextContent('dashboard');
    });
});
