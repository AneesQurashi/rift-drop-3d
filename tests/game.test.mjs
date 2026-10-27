import test from 'node:test';
import assert from 'node:assert/strict';
import { readFileSync } from 'node:fs';
const html=readFileSync(new URL('../index.html',import.meta.url),'utf8');
test('standalone contains the arcade game',()=>assert.match(html,/RIFT DROP 3D/i));
test('browser has no external dependency links',()=>assert.doesNotMatch(html,/<script[^>]+src=|<link[^>]+href=['"]https?:/i));
test('contains canvas scene',()=>assert.match(html,/<canvas[^>]+id=["']scene/));
test('has keyboard and pointer event controls',()=>{assert.match(html,/keydown/);assert.match(html,/pointerdown/);});
test('Android offline asset and browser build are identical',()=>{const android=readFileSync(new URL('../android-app/app/src/main/assets/index.html',import.meta.url),'utf8');assert.equal(android,html);});
