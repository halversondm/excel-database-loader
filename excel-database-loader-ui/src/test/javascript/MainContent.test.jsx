import React from 'react';
import { describe, expect, it, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import MainContent from '../../main/javascript/MainContent';
import '@testing-library/jest-dom';

// Mock child components

vi.mock('../../main/javascript/Dashboard', () => ({
    default: () => <div data-testid="dashboard">Dashboard</div>,
}));
vi.mock('../../main/javascript/Profile', () => ({
    default: () => <div data-testid="profile">Profile</div>,
}));
vi.mock('../../main/javascript/Settings', () => ({
    default: () => <div data-testid="settings">Settings</div>,
}));
vi.mock('../../main/javascript/Upload', () => ({
    default: () => <div data-testid="upload">Upload</div>,
}));

describe('MainContent', () => {
    it('renders Dashboard when activeView is "dashboard"', () => {
        render(<MainContent activeView="dashboard" />);
        expect(screen.getByTestId('dashboard')).toBeInTheDocument();
    });

    it('renders Profile when activeView is "profile"', () => {
        render(<MainContent activeView="profile" />);
        expect(screen.getByTestId('profile')).toBeInTheDocument();
    });

    it('renders Settings when activeView is "settings"', () => {
        render(<MainContent activeView="settings" />);
        expect(screen.getByTestId('settings')).toBeInTheDocument();
    });

    it('renders Upload when activeView is "upload"', () => {
        render(<MainContent activeView="upload" />);
        expect(screen.getByTestId('upload')).toBeInTheDocument();
    });

    it('renders welcome message for unknown activeView', () => {
        render(<MainContent activeView="unknown" />);
        expect(screen.getByText(/Welcome/i)).toBeInTheDocument();
        expect(
            screen.getByText(/Select an option from the sidebar/i)
        ).toBeInTheDocument();
    });

    it('renders welcome message when activeView is undefined', () => {
        render(<MainContent />);
        expect(screen.getByText(/Welcome/i)).toBeInTheDocument();
    });
});
