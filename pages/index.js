// pages/index.js
import React from 'react';
import Layout from '@theme/Layout';
import Mermaid from '@theme/Mermaid';
import Admonition from '@theme/Admonition';

export default function Home() {
  return (
    <Layout title="SOME CAMERA" description="Remote Camera App">
      <main style={{
    padding: '2rem',
    width: '100%',
    maxWidth: '1200px',
    margin: '0 auto',

    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  }}>
        <h2>SOME CAMERA</h2>

        <img
          src="https://github.com/treeentertainment/somecamera/blob/main/logo.png?raw=true"
          width="200"
          height="200"
          alt="SomeCamera Logo"
        />

        <div style={{ margin: '1rem 0' }}>
          <a href="https://github.com/treeentertainment/somecamera/actions/workflows/build.yml">
            <img
              src="https://img.shields.io/github/actions/workflow/status/treeentertainment/somecamera/build.yml?style=for-the-badge"
              alt="Build Status"
            />
          </a>
          <a href="https://github.com/treeentertainment/somecamera/actions/workflows/deploy.yml" style={{ marginLeft: '1rem' }}>
            <img
              src="https://img.shields.io/github/actions/workflow/status/treeentertainment/somecamera/deploy.yml?style=for-the-badge&logo=%20&label=deploy"
              alt="Deploy Status"
            />
          </a>
        </div>

        <Admonition type="note" title="NOTE">
          This repository is a fork of{' '}
          <a
            href="https://github.com/michaelzoech/remoteyourcam-usb"
            target="_blank"
            rel="noopener noreferrer"
          >
            michaelzoech/remoteyourcam-usb
          </a>.
        </Admonition>


        <Mermaid value={`
          graph LR;
            A[fa:fa-github Remote Your Cam OSS] -->|FORK| B[fa:fa-github SomeCamera]
            click A "https://github.com/michaelzoech/remoteyourcam-usb" "Go to Remote Your Cam OSS"
            click B "https://github.com/treeentertainment/somecamera" "Go to SomeCamera"
        `} />

        <h2>Features</h2>
        <table>
          <thead>
            <tr>
              <th>Feature</th>
              <th>Nikon</th>
              <th>Canon</th>
            </tr>
          </thead>
          <tbody>
            <tr><td>Capture photos</td><td>✅</td><td>✅</td></tr>
            <tr><td>Review photos</td><td>✅</td><td>✅</td></tr>
            <tr><td>Display exposure mode</td><td>✅</td><td>✅</td></tr>
            <tr><td>Bulb shooting</td><td>❌</td><td>✅</td></tr>
            <tr><td>Live View</td><td>✅</td><td>✅</td></tr>
            <tr><td>└ Histogram</td><td>❌</td><td>✅</td></tr>
            <tr><td>└ Drive Focus</td><td>✅</td><td>✅</td></tr>
            <tr><td>└ Adjustable capture duration</td><td>✅</td><td>✅</td></tr>
            <tr><td>└ Zoom &amp; Pan</td><td>✅</td><td>✅</td></tr>
            <tr><td>Gallery (with Zoom &amp; Pan)</td><td>✅</td><td>✅</td></tr>
            <tr><td>Picture Stream</td><td>✅</td><td>✅</td></tr>
            <tr><td>White balance</td><td>✅</td><td>✅</td></tr>
            <tr><td>Shutter speed</td><td>✅</td><td>✅</td></tr>
            <tr><td>ISO sensitivity</td><td>✅</td><td>✅</td></tr>
            <tr><td>Aperture priority</td><td>✅</td><td>✅</td></tr>
            <tr><td>Color temperature</td><td>✅</td><td>✅</td></tr>
            <tr><td>AF metering mode</td><td>✅</td><td>❌</td></tr>
            <tr><td>Select focus point</td><td>✅</td><td>❌</td></tr>
            <tr><td>Live View focus box</td><td>✅</td><td>❌</td></tr>
            <tr><td>Exposure compensation</td><td>✅</td><td>✅</td></tr>
          </tbody>
        </table>

        <h2>Progress</h2>
        <ul>
          <li>✅ Change UI to material design for android</li>
          <li>✅ Add live view continue after taking picture</li>
        </ul>
      </main>
    </Layout>
  );
}

