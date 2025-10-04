// docusaurus.config.js
/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'SomeCamera',
  tagline: 'SomeCamera',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'favicon.ico',
  organizationName: 'treeentertainment', // Usually GitHub org/user name.
  projectName: 'SomeCamera', // Usually repo name.
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          editUrl: 'https://github.com/treeentertainment/somecamera/edit/main/docs',
        },
      },
    ],
  ],
};

module.exports = config;
