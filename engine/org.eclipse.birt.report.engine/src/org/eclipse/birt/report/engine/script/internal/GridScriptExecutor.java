/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.engine.api.script.eventhandler.IAutoTextEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IGridEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IGridInstance;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Grid;
import org.eclipse.birt.report.engine.script.internal.instance.GridInstance;
import org.eclipse.birt.report.model.api.GridHandle;

public class GridScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( GridHandle gridHandle,
			ExecutionContext context )
	{
		try
		{
			IGrid grid = new Grid( gridHandle );
			IGridEventHandler eh = getEventHandler( gridHandle, context );
			if ( eh != null )
				eh.onPrepare( grid, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnCreate( ITableContent content,
			ExecutionContext context )
	{
		ReportItemDesign gridDesign = (ReportItemDesign) content
				.getGenerateBy( );
		if ( !needOnCreate( gridDesign ) )
		{
			return;
		}
		try
		{
			IGridInstance grid = new GridInstance( content, context );
			if ( handleJS( grid, gridDesign.getOnCreate( ), context ).didRun( ) )
				return;
			IGridEventHandler eh = getEventHandler( gridDesign, context );
			if ( eh != null )
				eh.onCreate( grid, context.getReportContext( ) );
		}
		catch ( Exception e )
		{
			addException( context, e, gridDesign.getHandle( ) );
		}
	}

	public static void handleOnRender( ITableContent content,
			ExecutionContext context )
	{
		ReportItemDesign gridDesign = (ReportItemDesign) content
				.getGenerateBy( );
		if ( !needOnRender( gridDesign ) )
		{
			return;
		}
		try
		{
			IGridInstance grid = new GridInstance( content, context );
			if ( handleJS( grid, gridDesign.getOnRender( ), context ).didRun( ) )
				return;
			IGridEventHandler eh = getEventHandler( gridDesign, context );
			if ( eh != null )
				eh.onRender( grid, context.getReportContext( ) );
		}
		catch ( Exception e )
		{
			addException( context, e, gridDesign.getHandle( ) );
		}
	}

	public static void handleOnPageBreak( ITableContent content,
			ExecutionContext context )
	{
		ReportItemDesign gridDesign = (ReportItemDesign) content
				.getGenerateBy( );
		if ( !needOnPageBreak( gridDesign ) )
		{
			return;
		}
		try
		{
			IGridInstance grid = new GridInstance( content, context );
			if ( handleJS( grid, gridDesign.getOnPageBreak( ), context )
					.didRun( ) )
				return;
			IGridEventHandler eh = getEventHandler( gridDesign, context );
			if ( eh != null )
				eh.onPageBreak( grid, context.getReportContext( ) );
		}
		catch ( Exception e )
		{
			addException( context, e, gridDesign.getHandle( ) );
		}
	}

	private static IGridEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		try
		{
			return (IGridEventHandler) getInstance( design, context );
		}
		catch ( ClassCastException e )
		{
			addClassCastException( context, e, design.getHandle( ),
					IGridEventHandler.class );
		}
		catch ( EngineException e )
		{
			addClassCastException( context, e, design.getHandle( ),
					IAutoTextEventHandler.class );
		}
		return null;
	}

	private static IGridEventHandler getEventHandler( GridHandle handle,
			ExecutionContext context )
	{
		try
		{
			return (IGridEventHandler) getInstance( handle, context );
		}
		catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle, IGridEventHandler.class );
		}
		catch ( EngineException e )
		{
			addClassCastException( context, e, handle,
					IAutoTextEventHandler.class );
		}
		return null;
	}
}
