import {describe, expect, it} from 'vitest';
import {fireEvent, render, screen} from '@testing-library/react';
import Profile from '../../main/javascript/Profile';
import '@testing-library/jest-dom';

describe('Profile', () => {
    it('allows typing in name and email fields', () => {
        render(<Profile/>);
        const nameInput = screen.getByPlaceholderText(/Enter your name/i);
        const emailInput = screen.getByPlaceholderText(/Enter your email/i);

        fireEvent.change(nameInput, {target: {value: 'John Doe'}});
        fireEvent.change(emailInput, {target: {value: 'john@example.com'}});

        expect(nameInput.value).toBe('John Doe');
        expect(emailInput.value).toBe('john@example.com');
    });
});
