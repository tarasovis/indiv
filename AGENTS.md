# AGENTS.md

## Project goal

Desktop Java Swing application for the following task:

Given a set of points on a plane and a set of circles, consider all triangles whose vertices are chosen from the set of points. Find the triangle with the **maximum perimeter among those triangles for which the number of circles completely outside the triangle is maximal**.

A circle is considered **outside** a triangle if the circle and the triangle have **no common points**.

The project is educational. The code should stay simple, readable, and suitable for a typical student coursework project.

---

## Core functional requirements

### Data input

The program must support:

1. **Mouse input**
   - **Left mouse button** adds a point.
   - **Right mouse button** creates a circle.
   - Circle creation must be **smooth**:
     - first right click sets the center;
     - while the mouse moves, the circle is redrawn with changing radius;
     - second right click fixes the circle.

2. **Keyboard input**
   - input of **two numbers** means a point: `x y`
   - input of **three numbers** means a circle: `x y r`

3. **File input/output**
   - save all current data to file;
   - load data from file;
   - example files must be available from the **File** menu.

---

## UI requirements

Keep the interface minimal.

### Must be present

- Menu **File**:
  - Open
  - Save
  - Example 1
  - Example 2
  - Exit
- Separate menu bar buttons/items:
  - **Keyboard input**
  - **Clear**
  - **Find triangle**

### Must NOT be present

- No **Tools** menu.
- No **Data** menu.
- No **Actions** menu.
- No coordinate axes.
- No coordinate plane/grid.
- No status bar at the bottom.
- No point coordinate labels near points.
- No dynamic color changes for circles during work.

### Coordinate system

Use normal screen coordinates:
- origin `(0, 0)` is at the **top-left corner**;
- `x` grows to the right;
- `y` grows downward.

### Result output

The result of triangle search must be shown in a **popup dialog window** (`JOptionPane`), not in a status line.

The dialog should report:
- triangle perimeter;
- number of circles outside the triangle.

---

## Geometry rules

### Points and triangles

- A triangle is valid only if its three vertices are **not collinear**.
- All triangles are formed only from the entered points.

### Circle outside triangle

A circle is outside a triangle only if:
- the center is outside the triangle **or** positioned anywhere not causing intersection,
- and the distance from the center to each triangle side is strictly greater than the radius whenever the perpendicular falls on the segment,
- and there is no intersection with triangle edges,
- and no triangle vertex lies on or inside the circle,
- and the circle does not touch the triangle.

Touching counts as **not outside**, because the statement says they must have **no common points**.

### Optimization rule

Selection order is:

1. maximize the number of circles outside the triangle;
2. among such triangles, choose the one with the **maximum perimeter**.

This priority must not be changed.

---

## Code style constraints

### Main style rule

**No method should exceed 25 lines.**

This is a hard requirement.

When adding logic:
- extract helper methods aggressively;
- prefer several small private methods;
- avoid long event handlers;
- avoid long `paintComponent` and search methods.

### Additional style preferences

- Keep classes small and responsibility-focused.
- Use clear English identifiers in code.
- Avoid deeply nested logic.
- Avoid duplication.
- Prefer simple OOP over clever abstractions.
- The project should remain easy to explain orally.

---

## Recommended class structure

Codex may reorganize code, but should preserve a simple structure close to this:

- `Main` — application window, menus, startup
- `DrawPanel` — drawing and mouse handling
- `PointData` — point model
- `CircleData` — circle model
- `TriangleData` — triangle model
- `GeometryUtils` — geometric calculations
- `FileManager` — loading/saving/examples
- `InputParser` — keyboard input parsing

This exact split is optional, but the code must stay simple and methods must stay short.

---

## Rendering requirements

### Points

- Draw as small filled circles.
- No coordinate text next to them.

### Circles

- Draw with one constant color.
- During right-button creation, preview must be shown smoothly while mouse moves.
- After second click, the circle is finalized.

### Result triangle

- After search, the found triangle should be visibly drawn.
- It may use a distinct color or thicker stroke.
- This highlight should remain simple and stable.

---

## File format expectations

Use a simple text format suitable for student work.

Recommended format:

- first line: number of points
- next lines: point coordinates
- then number of circles
- next lines: circle center and radius

Example:

```text
3
100 100
200 100
150 200
2
300 150 40
500 300 60
```

Keep parsing tolerant but simple.

---

## Behavior expectations for future changes

When modifying this project, always preserve:

1. minimal UI;
2. left click for point, right click for circle;
3. smooth circle preview before fixing;
4. popup result dialog;
5. no axes, labels, or status bar;
6. method length limit of 25 lines;
7. optimization priority: first circle count, then perimeter.

---

## What Codex should avoid

Do **not**:
- add JavaFX;
- replace Swing with another UI toolkit;
- introduce heavy frameworks;
- over-engineer patterns;
- add unnecessary animations;
- add toolbars, side panels, status bars, grids, or coordinate labels;
- merge too much logic into one giant class;
- create methods longer than 25 lines;
- change the problem statement or optimization order.

---

## Good change examples

Allowed improvements:
- refactor long methods into helpers;
- improve geometric correctness;
- improve file parsing;
- improve menu organization while keeping it minimal;
- add comments for coursework readability;
- add small validation messages;
- add unit-testable geometry helpers if they stay simple.

---

## Final instruction for agents

When continuing work on this repository:
- preserve the existing educational style;
- keep the implementation compact and readable;
- prefer straightforward solutions;
- strictly respect the 25-line-per-method rule;
- do not add visual or architectural complexity unless absolutely necessary.
