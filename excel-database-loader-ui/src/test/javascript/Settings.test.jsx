import React from 'react';
import { describe, expect, it } from 'vitest';
import { fireEvent, render, screen } from '@testing-library/react';
import Settings from '../../main/javascript/Settings';
import '@testing-library/jest-dom';

describe('Settings', () => {
    it('renders headings, checkboxes, and button', () => {
        render(<Settings />);
        expect(screen.getByText('Settings')).toBeInTheDocument();
        expect(screen.getByText('Preferences')).toBeInTheDocument();
        expect(screen.getByText('Enable notifications')).toBeInTheDocument();
        expect(screen.getByText('Dark mode')).toBeInTheDocument();
        expect(screen.getByText('Auto-save')).toBeInTheDocument();
        expect(
            screen.getByRole('button', { name: /Update Settings/i })
        ).toBeInTheDocument();
    });

    it('allows checking and unchecking preferences', () => {
        render(<Settings />);
        const checkboxes = screen.getAllByRole('checkbox');
        expect(checkboxes).toHaveLength(3);

        fireEvent.click(checkboxes[0]);
        expect(checkboxes[0].checked).toBe(true);

        fireEvent.click(checkboxes[0]);
        expect(checkboxes[0].checked).toBe(false);
    });
});
