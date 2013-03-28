package shrider.uhe.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLRenderer implements Renderer {

	private float[] light0Amb;
	private float[] light0Dif;
	private float[] light0Pos;
	private float[] matAmb;
	private float[] matSpec;
	
	private float[] transRot;
	private float[] transTran;
	private float[] transScale;
	
	private float[] cfWorld;
	
	boolean isAnimating;
	Context mCtx;

	public GLRenderer(Context parent){
		mCtx = parent;
		isAnimating = true;
		transRot = new float[16];
		
		for (int i=0;i<16;i+=5){
			cfWorld[i] = 1f;
			transRot[i] = 1f;
			transTran[i] = 1f;
			transScale[i] = 1f;
		}
			
		
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glClearColor(0, 0, 0, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 5, 0, 0, 0, 0, 0, 1f); //take a step back...or 5
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glMultMatrixf(cfWorld, 0);
		
		//begin the actual render
		gl.glPushMatrix();
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Pos, 0);
		gl.glPopMatrix();
		
		gl.glEnable(GL10.GL_LIGHTING);
		
		gl.glPushMatrix();
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
		// TODO Auto-generated method stub
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
		gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 20); //only if want shiny
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
	}

}
