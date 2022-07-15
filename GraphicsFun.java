import java.awt.*;
import javax.swing.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

class CNum {
	public double r;
	public double i;

	public CNum(double r, double i) {
		this.r = r;
		this.i = i;
	}

	public CNum times(CNum n) {
		double r = (this.r * n.r) + (this.i * n.i * -1);
		double i = (this.i * n.r) + (this.r * n.i);

		return new CNum(r, i);
	}

	public CNum add(CNum n) {
		return new CNum((this.r + n.r), (this.i + n.i));
	}

	public double abs() {
		return Math.hypot(this.r, this.i);
	}

	public static CNum convert(int x, int y, double startX, double dx, double dy) {
		return new CNum((startX + (x * dx)), (2 - (y * dy)));
	}
}

class MyCanvas extends JComponent {
	public static final double startX = -2;
	public static final double startY = 2;
	public static final double width = 4;
	public static final double height = 4;

	private int iterations;
	private int canvasWidth;
	private int canvasHeight;
	private double dx;
	private double dy;
	private Graphics g;
	private BufferedImage buffer;

	public MyCanvas(int width, int height, int iterations) {
		this.canvasWidth = width;
		this.canvasHeight = height;
		this.iterations = iterations;

		this.dx = this.width / (this.canvasWidth - 1);
		this.dy = this.height / (this.canvasHeight - 1);

		buffer = new BufferedImage(this.canvasWidth, this.canvasHeight, BufferedImage.TYPE_INT_RGB);
		this.render();

		JFrame frame = new JFrame("Mandelbrot Set");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.getContentPane().add(this);

		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void addNotify() {
		setPreferredSize(new Dimension(this.canvasWidth, this.canvasHeight));
	}

	@Override
	public void paint(Graphics g) {
		this.g = g;

		g.drawImage(buffer, 0, 0, null);
	}

	public void render() {
		for(int x = 0; x < this.canvasWidth; x++) {
			for(int y = 0; y < this.canvasHeight; y++) {
				int color = getPointColor(x, y);
				buffer.setRGB(x, y, color);
			}
		}
	}

	public int getPointColor(int x, int y) {
		CNum num = CNum.convert(x, y, this.startX, this.dx, this.dy);
		CNum z = num;
		int i;

		for(i = 0; i < this.iterations; i++) {
			z = z.times(z).add(num);

			if (z.abs() > 2.0) {
				break;
			}
		}

		if (i == iterations) {
			return 0x837EB1;
		} else if (i % 2 == 0) {
			return 0xF5FBA8;
		} else {
			return 0xFFD3AA;
		}
	}
}

public class GraphicsFun {
	public static void main(String... args) {
		int width = 1000;
		int height = 1000;
		int iterations = 256;

		MyCanvas canvas = new MyCanvas(width, height, iterations);

		canvas.render();
	}
}
