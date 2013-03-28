package shrider.uhe.graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLRenderer implements Renderer {

	private float[] light0Amb = {.4f, .4f, .4f, 1f};
	private float[] light0Dif = {1f, 1f, 1f, 1f};
	private float[] light0Pos = {0f, 0f, 0f, 1f};
	private float[] matAmb = {.3f, .3f, .3f, 1f};
	private float[] matSpec = {.7f, 1f, .7f, 1f};
	
	private float[] transRot;
	private float[] transTran;
	private float[] transScale;
	
	private float[] cfWorld;
	
	//short amount=8;
//	short faces=3;
	private FloatBuffer bufVer, bufNorm, bufCol;
	private ShortBuffer bufDraw;
	ByteBuffer buff;
	private float[] arrVer = {	.5f,-.5f,0f, 	//0
								.5f,.5f,0f,	//1
								-.5f,.5f,0f,	//2
								-.5f,-.5f,0f,		//3
								-.5f,.5f,-1f,	//4
								-.5f,-.5f,-1f,	//5
								-.6f,-.6f,-1f,	//6
								-.5f,-.57f,-1f		//7
							 };
	private short[] arrDraw = {	0,2,1,
								0,1,3,
								7,5,6,
								7,5,4
							  };
	private float[] arrNorm = {	-1f,1f,1f,
								-1f,-1f,1f,
								1f,-1f,1f,
								1f,1f,1f,
								-1f,1f,-1f,
								-1f,-1f,-1f,
								1f,-1f,-1f,
								1f,1f,-1f
								};
	private float[] arrCol = {	1f,0,0,1f,
								0,1f,0,1f,
								0,0,1f,1f,
								1f,1f,0,1f,
								1f,0,1f,1f,
								0f,1f,1f,1f,
								0f,0f,0f,1f,
								.4f,1f,1f,1f
							 };
	
	boolean isAnimating;
	Context mCtx;

	public GLRenderer(Context parent){
		super();
		mCtx = parent;
		isAnimating = true;
		transRot = new float[16];
		transTran = new float[16];
		transScale = new float[16];
		cfWorld = new float[16];
		
		for (int i=0;i<16;i+=5){ //everything in here becomes I
			cfWorld[i] = 1f;
			transRot[i] = 1f;
			transTran[i] = 1f;
			transScale[i] = 1f;
		}
			
	}
	@Override
	public synchronized void onDrawFrame(GL10 gl) {
		gl.glClearColor(1f, .2f, .2f, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		
		GLU.gluLookAt(gl, 0, 0, 8,
				0, 0, 0, 
				0, 0, 1f); //take a step back...or 8
		gl.glColor4f(1.0f,1.0f,1.0f,1.0f);
		cfWorld[4] = .4f;
		gl.glMultMatrixf(cfWorld, 0);
		
		//begin the actual render
		gl.glPushMatrix();
		gl.glTranslatef(.3f, .4f, -.5f);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Pos, 0);
		gl.glPopMatrix();
		
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glPushMatrix();
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,bufVer);
		gl.glColorPointer(4,GL10.GL_FLOAT,0,bufCol);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, bufNorm);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, arrDraw.length, GL10.GL_UNSIGNED_SHORT, bufDraw);
		
		gl.glPopMatrix();
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		boolean isLandscape = width > height;
		float aspect = (float) width / height;
		GLU.gluPerspective(gl, 60, aspect, .1f, 20.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, light0Amb, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, light0Dif, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Pos, 0);
		
		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, light0Amb, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_AND_DIFFUSE, matAmb,0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, matSpec, 0);
		gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 10); //only if want shiny
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		initArrays();
		
	}

	private void initArrays(){
		buff = ByteBuffer.allocateDirect(arrVer.length * 4);
		buff.order(ByteOrder.nativeOrder());
		bufVer = buff.asFloatBuffer();
		buff = ByteBuffer.allocateDirect(arrCol.length * 4);
		buff.order(ByteOrder.nativeOrder());
		bufCol = buff.asFloatBuffer();
		buff = ByteBuffer.allocateDirect(arrNorm.length * 4);
		buff.order(ByteOrder.nativeOrder());
		bufNorm = buff.asFloatBuffer();
		buff = ByteBuffer.allocateDirect(arrDraw.length * 2);
		buff.order(ByteOrder.nativeOrder());
		bufDraw = buff.asShortBuffer();
		
		
		bufVer.put(arrVer);
		bufCol.put(arrCol);
		bufDraw.put(arrDraw);
		bufNorm.put(arrNorm);
        
		bufVer.position(0);
		bufCol.position(0);
		bufNorm.position(0);
		bufDraw.position(0);
	}
}
