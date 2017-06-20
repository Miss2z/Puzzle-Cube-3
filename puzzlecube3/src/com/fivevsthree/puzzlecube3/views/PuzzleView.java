package com.fivevsthree.puzzlecube3.views;

import java.util.Comparator;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
//import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
//import com.badlogic.gdx.graphics.g3d.lights.Lights;
//import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
//import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.fivevsthree.puzzlecube3.models.PuzzleModel;

public class PuzzleView extends View<PuzzleModel> {

	public class CubeSort implements Comparator<ModelInstance> {

		private Vector3 cameraPosition, position1, position2;

		public CubeSort() {
			cameraPosition = new Vector3();
			position1 = new Vector3();
			position2 = new Vector3();
		}

		public void setCameraPosition(Vector3 position) {
			cameraPosition.set(position);
		}

		@Override
		public int compare(ModelInstance o1, ModelInstance o2) {
			o1.transform.getTranslation(position1);
			o2.transform.getTranslation(position2);

			return Float.compare(cameraPosition.dst(position1),
					cameraPosition.dst(position2));
		}

	}

	private Model cubeModelLow, cubeModelHigh;
	private ModelBatch batch;
	//private Lights lights;
	//private DirectionalLight directionalLight;
	private Texture mask;

	Vector3 cubePosition, cornerPosition;

	private CubeSort modelInstanceSort;

	private Array<ModelInstance> corners;

	public PuzzleView(PuzzleModel model) {
		super(model);
	}

	@Override
	public void create() {
		batch = new ModelBatch();

		model.camera = new PerspectiveCamera();
		model.camera.position.set(1, 1, 1);
		model.camera.lookAt(0, 0, 0);
		model.camera.near = 1f;
		model.camera.far = 100f;
		model.camera.update();

		model.cubes = new Array<ModelInstance>();

		//directionalLight = new DirectionalLight();
		//directionalLight.color.set(0.6f, 0.6f, 0.6f, 1);

		//lights = new Lights();
		//lights.ambientLight.set(1f, 1f, 1f, 1);
		//lights.add(directionalLight);

		modelInstanceSort = new CubeSort();
		corners = new Array<ModelInstance>();

		cubePosition = new Vector3();
		cornerPosition = new Vector3();
	}

	@Override
	public void load(AssetManager assets) {
		mask = assets.get("textures/mask.png");
		mask.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		cubeModelLow = assets.get("models/cube-low.g3db");
		for (NodePart part : cubeModelLow.getNode("default").parts) {
			part.meshPart.id = part.material.id;
			part.material.set(ColorAttribute.createDiffuse(Color.BLACK));
		}

		cubeModelHigh = assets.get("models/cube-high.g3db");
		for (NodePart part : cubeModelHigh.getNode("default").parts) {
			part.meshPart.id = part.material.id;
			part.material.set(ColorAttribute.createDiffuse(Color.BLACK));
		}
	}

	@Override
	public void show() {
		assemblePuzzle();

		// Zoom to fit the entire puzzle on the screen
		float currentDistance = model.camera.position.dst(Vector3.Zero);
		float targetDistance = new Vector3(1, 1, 1).scl(model.puzzleSize * -3f)
				.dst(Vector3.Zero);

		model.camera.translate(new Vector3(model.camera.direction)
				.scl(currentDistance - targetDistance));
		model.camera.update();
	}

	@Override
	public void render(float delta) {
		// Move the light with the camera
		//directionalLight.direction.set(model.camera.direction);

		// Sort cubes so we can render those closest to camera first
		modelInstanceSort.setCameraPosition(model.camera.position);
		model.cubes.sort(modelInstanceSort);

		// Sort corners by distance from camera
		corners.sort(modelInstanceSort);

		batch.begin(model.camera);

		// Get the position of the corner closest to the camera
		corners.get(0).transform.getTranslation(cornerPosition);

		for (ModelInstance cube : model.cubes) {
			// Get the position of the current cube
			cube.transform.getTranslation(cubePosition);

			// Only render cubes that are on the 3 puzzle faces adjacent to the
			// corner closest to the camera. Other sides won't be visible so why
			// render them.
			if (cornerPosition.x == cubePosition.x
					|| cornerPosition.y == cubePosition.y
					|| cornerPosition.z == cubePosition.z) {
				if (model.quality == 0) {
					batch.render(cube);
				} else {
					batch.render(cube/*, lights*/);
				}
			}
		}

		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		model.camera.fieldOfView = height / 20;
		model.camera.viewportWidth = width;
		model.camera.viewportHeight = height;
		model.camera.update();
	}

	@Override
	public void dispose() {
		if (batch != null) {
			batch.dispose();
		}
		if (model.cubes != null) {
			model.cubes.clear();
			model.cubes.shrink();
		}
		if (corners != null) {
			corners.clear();
			corners.shrink();
		}
	}

	private void assemblePuzzle() {
		model.cubes.clear();
		model.cubes.shrink();

		corners.clear();

		Model cubeModel = (model.quality > 1) ? cubeModelHigh : cubeModelLow;

		// Puzzle needs to be at least 2x2x2
		model.puzzleSize = Math.max(2, model.puzzleSize);

		// Get the size of a single cube
		BoundingBox box = new BoundingBox();
		cubeModel.calculateBoundingBox(box);

		float xSize = Math.round(box.getDimensions().x);
		float ySize = Math.round(box.getDimensions().y);
		float zSize = Math.round(box.getDimensions().z);

		// Cube origins remain within this limit
		// Cube origin is center of cube
		float xLimit = (((float) model.puzzleSize / 2) - 0.5f) * xSize;
		float yLimit = (((float) model.puzzleSize / 2) - 0.5f) * ySize;
		float zLimit = (((float) model.puzzleSize / 2) - 0.5f) * zSize;

		// Empty space inside puzzle up to inner facing edges of cubes
		float xInner = xLimit - (xSize / 2);
		float yInner = yLimit - (ySize / 2);
		float zInner = zLimit - (zSize / 2);

		// Bounding box for empty space inside puzzle
		BoundingBox innerBounds = new BoundingBox(new Vector3(-xInner, -yInner,
				-zInner), new Vector3(xInner, yInner, zInner));

		for (float x = -xLimit; x <= xLimit; x += xSize) {
			for (float y = -yLimit; y <= yLimit; y += ySize) {
				for (float z = -zLimit; z <= zLimit; z += zSize) {

					// Do not add a cube inside the puzzle
					if (innerBounds.contains(new Vector3(x, y, z))) {
						continue;
					}

					// Cube will be on a face of the puzzle so add it
					ModelInstance cube = new ModelInstance(cubeModel);

					// Apply sticker materials
					if (y == yLimit) {
						cube.getMaterial("Top").set(
								ColorAttribute.createDiffuse(model.topColor),
								TextureAttribute.createDiffuse(mask));
					} else if (y == -yLimit) {
						cube.getMaterial("Bottom")
								.set(ColorAttribute
										.createDiffuse(model.bottomColor),
										TextureAttribute.createDiffuse(mask));
					}

					if (x == xLimit) {
						cube.getMaterial("Right").set(
								ColorAttribute.createDiffuse(model.rightColor),
								TextureAttribute.createDiffuse(mask));
					} else if (x == -xLimit) {
						cube.getMaterial("Left").set(
								ColorAttribute.createDiffuse(model.leftColor),
								TextureAttribute.createDiffuse(mask));
					}

					if (z == zLimit) {
						cube.getMaterial("Front").set(
								ColorAttribute.createDiffuse(model.frontColor),
								TextureAttribute.createDiffuse(mask));
					} else if (z == -zLimit) {
						cube.getMaterial("Back").set(
								ColorAttribute.createDiffuse(model.backColor),
								TextureAttribute.createDiffuse(mask));
					}

					// Move the cube to the correct position in the puzzle
					cube.transform.setToTranslation(x, y, z);
					model.cubes.add(cube);

					if (Math.abs(x) == xLimit && Math.abs(y) == yLimit
							&& Math.abs(z) == zLimit) {
						corners.add(cube);
					}
				}
			}
		}
	}

}
