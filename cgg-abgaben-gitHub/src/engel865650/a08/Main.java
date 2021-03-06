package engel865650.a08;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cgtools.Mat4;
import cgtools.Random;
import cgtools.Vec3;
import engel865650.Image;
import engel865650.a08.CObscura;
import engel865650.a08.Hit;
import engel865650.a03.Ray;

public class Main {

	private static final int width = 1280;
	private static final int height = 720;
	public static Image image = null;
	public static Globe globe = null;
	private static CObscura obscura = null;
	private static Vec3 color, background, shade = null;
	static Material material;

	public static void main(String[] args) {
		a08_01();
		a08_02();
	}

	public static void a08_01() {
		image = new Image(width, height);
		obscura = new CObscura(Math.PI / 3, image.getWidth(), image.getHeight(), new Vec3(-14, 215, 0),
				Mat4.translate(-10, 10, -17));
		/*
		 * all Scenes together
		 */
		List<Shape> allScenes = new ArrayList<Shape>();

		/*
		 * first Scenech
		 */
		List<Shape> oneScene = new ArrayList<Shape>();
		double anzahl = 5;
		for (double i = -5; i < anzahl; i++) {
			for (double j = -5; j < anzahl; j++) {
				Shape globe1 = new Globe(new Vec3(-13 + i, 6 + j, j), 1 * Random.random(),
						new DiffuseMaterial(new Vec3(Random.random() * 2, Random.random() * 1, Random.random() + 2)));
				oneScene.add(globe1);
			}
		}
		int anzahl1 = 8;
		for (int i = 0; i < anzahl1; i++) {
			Group group = new Group(oneScene, new Transform(Mat4.translate(new Vec3(2 * i, 2 * i, -8 * i))));
			allScenes.add(group);
		}

		/*
		 * second Scene
		 */
		List<Shape> secondScene = new ArrayList<Shape>();
		double anzahl2 = 3;
		for (double i = -3; i < anzahl2; i++) {
			for (double j = -3; j < anzahl2; j++) {
				Shape globe1 = new Globe(new Vec3(Random.random() * i, 4 + (j * -4), j), 0.5,
						new DiffuseMaterial(new Vec3(1, 1, 0)));
				secondScene.add(globe1);
			}
		}
		int anzahl3 = 10;
		for (int i = 0; i < anzahl3; i++) {
			Group group2 = new Group(secondScene, new Transform(Mat4.translate(new Vec3(2 * i, 2 * i, -8 * i))));
			allScenes.add(group2);
		}

		/*
		 * third Scene
		 */
		List<Shape> thirdScene = new ArrayList<Shape>();
		Shape blueBall = new Globe(new Vec3(9, 1, 1), 2, new DiffuseMaterial(Vec3.blue));
		Shape greenBall = new Globe(new Vec3(7, 3.5, 2), 1, new DiffuseMaterial(Vec3.green));
		thirdScene.add(blueBall);
		thirdScene.add(greenBall);
		int anzahl4 = 5;
		for (int i = 0; i < anzahl4; i++) {
			Group group3 = new Group(thirdScene,
					new Transform(Mat4.translate(new Vec3(2 * i, Math.pow(Random.random(), 2) * i, -3 * i))));
			allScenes.add(group3);
		}

		/*
		 * fourth Scene
		 */
		List<Shape> fourthScene = new ArrayList<Shape>();
		Shape redCylinder = new Cylinder(new Vec3(1, 0, 2), 1, 4, new DiffuseMaterial(Vec3.red));
		Shape randomCylinder = new Cylinder(new Vec3(1.5, 1, 16), 1, 15,
				new DiffuseMaterial(new Vec3(Random.random() * 1.5, Random.random() * -3, Random.random() + 3)));
		Shape greenCylinder = new Cylinder(new Vec3(1.5, 15, 16), 0.5, 4, new DiffuseMaterial(Vec3.green));
		fourthScene.add(redCylinder);
		fourthScene.add(randomCylinder);
		fourthScene.add(greenCylinder);
		Group group4 = new Group(fourthScene, new Transform(Mat4.translate(new Vec3(2, 3, 5))));
		allScenes.add(group4);

		// everything together
		Plane plane = new Plane(new Vec3(0.0, -1.0, 0.0), new Vec3(0.0, 1.8, 0.0),
				new PolishedMetalMaterial(new Vec3(0.7, 0.7, 0.7), 0.01));
		Background background = new Background(new BackgroundMaterial());
		Shape glassBall = new Globe(new Vec3(-3, 7, 4), 3, new GlassMaterial(1));
		allScenes.add(plane);
		allScenes.add(background);
		allScenes.add(glassBall);
		Group allGroupes = new Group(allScenes, new Transform(Mat4.rotate(0, 1, 0, -155)));
		raytrace1(obscura, allGroupes, 10);
	}

	public static Image raytrace1(CObscura obscura, Group group, double sampler) {
		for (int x = 0; x != width; x++) {
			for (int y = 0; y != height; y++) {
				color = new Vec3(0, 0, 0);
				for (int xi = 0; xi < sampler; xi++) {
					for (int yi = 0; yi < sampler; yi++) {
						double rx = Random.random();
						double ry = Random.random();
						double xs = x + (xi + rx) / sampler;
						double ys = y + (yi + ry) / sampler;
						Ray currentRay = obscura.generate(xs, ys);
						shade = calculateRadiance(group, currentRay, 3.8);
						background = Vec3.divide(shade, sampler * sampler);
						color = Vec3.add(color, background);
					}
				}
				image.setPixel(x, y, color);
			}
		}
		write(image, "doc/a08-1.png");
		return image;
	}

	public static void a08_02() {
		image = new Image(width, height);
		obscura = new CObscura(Math.PI / 3, image.getWidth(), image.getHeight(), new Vec3(-41, 215, 0),
				Mat4.translate(-40, 60, -20));
		/*
		 * all Scenes together
		 */
		List<Shape> allScenes = new ArrayList<Shape>();

		/*
		 * first Scenech
		 */
		List<Shape> oneScene = new ArrayList<Shape>();
		double anzahl = 5;
		for (double i = -5; i < anzahl; i++) {
			for (double j = -5; j < anzahl; j++) {
				Shape globe1 = new Globe(new Vec3(-13 + i, 6 + j, j), 1 * Random.random(),
						new DiffuseMaterial(new Vec3(Random.random() * 2, Random.random() * 1, Random.random() + 2)));
				oneScene.add(globe1);
			}
		}
		int anzahl1 = 8;
		for (int i = 0; i < anzahl1; i++) {
			Group group = new Group(oneScene, new Transform(Mat4.translate(new Vec3(2 * i, 2 * i, -8 * i))));
			allScenes.add(group);
		}

		/*
		 * second Scene
		 */
		List<Shape> secondScene = new ArrayList<Shape>();
		double anzahl2 = 3;
		for (double i = -3; i < anzahl2; i++) {
			for (double j = -3; j < anzahl2; j++) {
				Shape globe2 = new Globe(new Vec3(Random.random() * i, 4 + (j * -4), j), 0.5,
						new DiffuseMaterial(new Vec3(1, 1, 0)));
				secondScene.add(globe2);
			}
		}
		int anzahl3 = 10;
		for (int i = 0; i < anzahl3; i++) {
			Group group2 = new Group(secondScene, new Transform(Mat4.translate(new Vec3(2 * i, 2 * i, -8 * i))));
			allScenes.add(group2);
		}

		/*
		 * third Scene
		 */
		List<Shape> thirdScene = new ArrayList<Shape>();
		Shape blueBall = new Globe(new Vec3(9, 1, 1), 2, new DiffuseMaterial(Vec3.blue));
		Shape greenBall = new Globe(new Vec3(7, 3.5, 2), 1, new DiffuseMaterial(Vec3.green));
		thirdScene.add(blueBall);
		thirdScene.add(greenBall);
		int anzahl4 = 5;
		for (int i = 0; i < anzahl4; i++) {
			Group group3 = new Group(thirdScene,
					new Transform(Mat4.translate(new Vec3(2 * i, Math.pow(Random.random(), 2) * i, -3 * i))));
			allScenes.add(group3);
		}

		/*
		 * fourth Scene
		 */
		List<Shape> fourthScene = new ArrayList<Shape>();
		Shape redCylinder = new Cylinder(new Vec3(1, 0, 2), 1, 4, new DiffuseMaterial(Vec3.red));
		Shape randomCylinder = new Cylinder(new Vec3(1.5, 1, 16), 1, 15,
				new DiffuseMaterial(new Vec3(Random.random() * 1.5, Random.random() * -3, Random.random() + 3)));
		Shape greenCylinder = new Cylinder(new Vec3(1.5, 15, 16), 0.5, 9, new DiffuseMaterial(Vec3.green));
		fourthScene.add(redCylinder);
		fourthScene.add(randomCylinder);
		fourthScene.add(greenCylinder);
		Group group4 = new Group(fourthScene, new Transform(Mat4.translate(new Vec3(2, 3, 5))));
		allScenes.add(group4);

		// everything together
		Plane plane = new Plane(new Vec3(0.0, -1.0, 0.0), new Vec3(0.0, 1.8, 0.0),
				new PolishedMetalMaterial(new Vec3(0.7, 0.7, 0.7), 0.01));
		Background background = new Background(new BackgroundMaterial());
		Shape glassBall = new Globe(new Vec3(-3, 7, 4), 3, new GlassMaterial(1));
		allScenes.add(plane);
		allScenes.add(background);
		allScenes.add(glassBall);
		Group allGroupes = new Group(allScenes, new Transform(Mat4.rotate(0, 1, 0, -210)));
		raytrace2(obscura, allGroupes, 10);
	}

	public static Image raytrace2(CObscura obscura, Group group, double sampler) {
		for (int x = 0; x != width; x++) {
			for (int y = 0; y != height; y++) {
				color = new Vec3(0, 0, 0);
				for (int xi = 0; xi < sampler; xi++) {
					for (int yi = 0; yi < sampler; yi++) {
						double rx = Random.random();
						double ry = Random.random();
						double xs = x + (xi + rx) / sampler;
						double ys = y + (yi + ry) / sampler;
						Ray currentRay = obscura.generate(xs, ys);
						shade = calculateRadiance(group, currentRay, 3.8);
						background = Vec3.divide(shade, sampler * sampler);
						color = Vec3.add(color, background);
					}
				}
				image.setPixel(x, y, color);
			}
		}
		write(image, "doc/a08-2.png");
		return image;
	}

	public static Vec3 calculateRadiance(Shape scene, Ray ray, double depth) {
		if (depth == 0) {
			return Vec3.black;
		}
		Hit hit = scene.intersect(ray);
		if (hit.getMaterial().scatteredRay(ray, hit) != null) {
			return Vec3.add(hit.getMaterial().emission(ray, hit), Vec3.multiply(hit.getMaterial().albedo(ray, hit),
					calculateRadiance(scene, hit.getMaterial().scatteredRay(ray, hit), depth - 1)));
		}

		return hit.getMaterial().emission(ray, hit);
	}

	static void write(Image image, String filename) {
		try {
			image.write(filename);
			System.out.println("Wrote image: " + filename);
		} catch (IOException error) {
			System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
		}
	}
}