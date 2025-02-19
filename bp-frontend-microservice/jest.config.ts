module.exports = {
    preset: 'jest-preset-angular',
    setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
    testMatch: ['**/+(*.)+(spec).+(ts)?(x)'],
    collectCoverage: true,
    coverageReporters: ['html', 'text-summary'],
    coverageDirectory: 'coverage/jest',
  };
  